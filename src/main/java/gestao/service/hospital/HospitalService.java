package gestao.service.hospital;

import static java.util.stream.Collectors.toList;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.exception.hospital.NearestHospitalNotFoundException;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.repository.hospital.HospitalRepository;
import gestao.util.geo.GeoApi;

@Service
public class HospitalService {

	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private GeoApi geoApi;

	public Hospital createHospital(HospitalDto hospitalDto) {
		Hospital newHospital = Hospital.createFromDto(hospitalDto);
		return this.save(newHospital);
	}

	public Hospital save(Hospital hospital) {
		return this.hospitalRepository.save(hospital);
	}

	public Hospital findById(Long id) {
		return hospitalRepository.findById(id).orElseThrow(HospitalNotFoundException::new);
	}

	public void verifyIfExistsById(Long id) {
		this.findById(id);
	}

	public List<Hospital> findAll() {
		return hospitalRepository.findAll();
	}

	public void delete(Long id) {
		hospitalRepository.delete(this.findById(id));
	}

	public Hospital update(Long hospitalId, HospitalDto hospitalDto) {
		Hospital hospital = this.findById(hospitalId);
		hospital.updateFromDto(hospitalDto);
		return hospitalRepository.save(hospital);
	}

	public ProductItem orderProductFromNearestHospitals(Long hospitalId, Product product, Integer amount) {
		Hospital originHospital = this.findById(hospitalId);

		List<Hospital> nearestHospitals = this.findNearestHospitals(originHospital);

		Hospital nearestHospital = nearestHospitals.stream().filter((hospital) -> hospital.reduceStock(product, amount))
				.findFirst().orElseThrow(() -> new NearestHospitalNotFoundException());

		ProductItem productItem = originHospital.addProductInStock(product, amount);

		this.save(originHospital);
		this.save(nearestHospital);

		return productItem;
	}

	public List<Hospital> findNearestHospitals(Hospital hospital) {

		List<Hospital> hospitals = this.hospitalRepository.findAllByIdNot(hospital.getId());

		return findNearestHospitals(hospitals, hospital.getAddress());
	}

	public List<Hospital> findNearestHospitals(Address origin) {

		return this.findNearestHospitals(this.findAll(), origin);
	}

	@Cacheable(value = "nearestHospitals")
	private List<Hospital> findNearestHospitals(List<Hospital> hospitals, Address origin) {
		String[] destinations = this.getHospitalsFormattedAddresses(hospitals);

		List<Long> distances = geoApi.getDistances(origin.getFormattedAddress(), destinations);

		if (distances.size() == 0) {
			throw new NearestHospitalNotFoundException();
		}
		return this.getHospitalsSortedByDistance(hospitals, distances);
	}

	private List<Hospital> getHospitalsSortedByDistance(List<Hospital> hospitals, List<Long> distances) {
		return IntStream.range(0, distances.size()).boxed()
				.map((i) -> new AbstractMap.SimpleEntry<>(hospitals.get(i), distances.get(i)))
				.sorted(Comparator.comparing(AbstractMap.SimpleEntry::getValue))
				.map(AbstractMap.SimpleEntry::getKey)
				.collect(Collectors.toList());
	}

	private String[] getHospitalsFormattedAddresses(List<Hospital> hospitals) {
		
		return hospitals.stream().map((hospital) -> hospital.getAddress()
				.getFormattedAddress()).toArray(String[]::new);
	}


	private List<Hospital> sortHospitalsByDistanceFromAnOrigin(List<Hospital> hospitals, Address origin) {
		List<Long> distances = geoApi.getDistances(origin.getFormattedAddress(),
				this.getHospitalsFormattedAddresses(hospitals));

		if (distances.size() == 0) {
			throw new NearestHospitalNotFoundException();
		}

		ArrayList<Long> arrayOfDistances = new ArrayList<>(distances);

		ArrayList<Hospital> arrayOfHospitals = new ArrayList<>(hospitals);

		return IntStream.range(0, hospitals.size() - 1).boxed().sorted((a, b) -> {
			if (arrayOfDistances.get(a) < arrayOfDistances.get(b)) {
				return -1;
			}

			if (arrayOfDistances.get(a) > arrayOfDistances.get(b)) {
				return 1;
			}

			return 0;
		}).<Hospital>map(i -> arrayOfHospitals.get(i)).collect(toList());
	}

}

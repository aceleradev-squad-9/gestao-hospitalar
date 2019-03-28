package gestao.service.hospital;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.exception.hospital.NearestHospitalNotFoundException;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
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

	public Hospital findNearestHospital(Hospital hospital) {

		List<Hospital> hospitals = this.hospitalRepository.findAllByIdNot(hospital.getId());

		return findNearestHospital(hospitals, hospital.getAddress());
	}

	public Hospital findNearestHospital(Address origin) {

		return this.findNearestHospital(this.findAll(), origin);
	}

	public String[] getHospitalsFormattedAddresses(List<Hospital> hospitals){
		return hospitals.stream()
			.map((hospital) -> hospital.getAddress().getFormattedAddress())
			.toArray(String[]::new);
	}

	@Cacheable(value = "nearestHospital")
	private Hospital findNearestHospital(List<Hospital> hospitals, Address origin) {
		String[] destinations = this.getHospitalsFormattedAddresses(hospitals);

		Integer indexOfMin = geoApi.getIndexOfMinorDistance(origin.getFormattedAddress(), destinations);

		if (indexOfMin < 0) {
			throw new NearestHospitalNotFoundException();
		}

		return hospitals.get(indexOfMin);
	}

	private List<Hospital> findHospitalsOrderedByDistance(List<Hospital> hospitals, Address origin){
		List<Long> distances = geoApi.getDistances(
			origin.getFormattedAddress(), 
			this.getHospitalsFormattedAddresses(hospitals)	
		);

		if(distances.size() == 0){
			throw new NearestHospitalNotFoundException();
		}

		ArrayList<Long> arrayOfDistances = new ArrayList<>(distances);

		ArrayList<Hospital> arrayOfHospitals = new ArrayList<>(hospitals);			

		return IntStream.range(1, hospitals.size())
			.boxed()
			.sorted((a,b) -> {
				if(arrayOfDistances.get(a) < arrayOfDistances.get(b)){
					return -1;
				}

				if(arrayOfDistances.get(a) > arrayOfDistances.get(b)){
					return 1;
				}

				return 0;
			})
			.<Hospital>map(i -> arrayOfHospitals.get(i))
			.collect(toList());
	}

}

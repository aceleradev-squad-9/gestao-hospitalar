package gestao.service.hospital;

import java.util.List;

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

	@Cacheable(value = "nearestHospital")
	private Hospital findNearestHospital(List<Hospital> hospitals, Address origin) {

		String[] destinations = hospitals.stream().map((hospital) -> hospital.getAddress().getFormattedAddress())
				.toArray(String[]::new);

		Integer indexOfMin = geoApi.getIndexOfMinorDistance(origin.getFormattedAddress(), destinations);

		if (indexOfMin < 0) {
			throw new NearestHospitalNotFoundException();
		}

		return hospitals.get(indexOfMin);
	}

}

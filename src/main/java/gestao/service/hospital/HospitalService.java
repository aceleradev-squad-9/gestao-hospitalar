package gestao.service.hospital;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.model.patient.LocalizationDto;
import gestao.repository.hospital.HospitalRepository;

@Service
public class HospitalService {

	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private HospitalGeoService hospitalGeoService;

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

	public Page<Hospital> findAll(Pageable pageable) {
		return hospitalRepository.findAll(pageable);
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

	public List<Hospital> findNearestHospitals(Hospital hospital) {
		List<Hospital> hospitals = this.hospitalRepository.findAllByIdNot(hospital.getId());
		return this.hospitalGeoService.findNearestHospitals(hospitals, hospital.getAddress());
	}

	public List<Hospital> findNearestHospitals(LocalizationDto localizationDto) {
		return this.hospitalGeoService.findNearestHospitals(this.findAll(), localizationDto);
	}

	public List<Hospital> findNearestHospitals(Address address) {
		return this.hospitalGeoService.findNearestHospitals(this.findAll(), address);
	}

}

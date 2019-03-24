package gestao.service.hospital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.repository.hospital.HospitalRepository;

@Service
public class HospitalService {

	@Autowired
	HospitalRepository hospitalRepository;

	public Hospital createHospital(HospitalDto hospitalDto) {
		Hospital newHospital = Hospital.createFromDto(hospitalDto);
		return this.save(newHospital);
	}

	public Hospital save(Hospital hospital) {
		return this.hospitalRepository.save(hospital);
	}

	public Hospital findById(Long id) {
		return hospitalRepository.findById(id).orElseThrow(() -> new HospitalNotFoundException());
	}

	public void verifyIfExistsById(Long id) {
		if (!this.existsById(id)) {
			throw new HospitalNotFoundException();
		}
	}

	private Boolean existsById(Long id) {
		return hospitalRepository.existsById(id);
	}

	public Iterable<Hospital> findAll() {
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

}

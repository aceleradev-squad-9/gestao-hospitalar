package gestao.service.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gestao.exception.patient.PatientAlreadyHasCheckInOnHospitalException;
import gestao.exception.patient.PatientAlreadyHasCheckOutOnHospitalException;
import gestao.exception.patient.PatientNotFoundException;
import gestao.model.hospital.Hospital;
import gestao.model.patient.Patient;
import gestao.model.person.Person;
import gestao.repository.patient.PatientRepository;

/**
 * Classe responsável pela implementação dos serviços relacionados ao paciente.
 * 
 * @author silasalvares
 *
 */
@Service
public class PatientService {

	@Autowired
	private PatientRepository repository;

	public Patient checkIn(Person person, Hospital hospital) {

		if (existsPersonWithCheckInOnHospital(person, hospital)) {
			throw new PatientAlreadyHasCheckInOnHospitalException();
		}

		Patient patient = hospital.checkIn(this.countNumberOfBedsOccupied(hospital), person);

		return this.repository.save(patient);
	}

	public Patient checkOut(Long patientId) {
		Patient patient = this.findById(patientId);

		if (patient.hasCheckedOut()) {
			throw new PatientAlreadyHasCheckOutOnHospitalException();
		}
		
		patient.doCheckOut();

		return this.repository.save(patient);
	}

	private Boolean existsPersonWithCheckInOnHospital(Person person, Hospital hospital) {
		return this.repository.existsByHospitalAndPersonAndTimeCheckOutIsNull(hospital, person);
	}

	public Page<Patient> findHospitalPatients(Hospital hospital, Pageable pageable) {
		return this.repository.findAllByHospitalAndTimeCheckOutIsNull(pageable, hospital);
	}

	public Long countNumberOfBedsOccupied(Hospital hospital) {
		return this.repository.countByHospitalAndTimeCheckOutIsNull(hospital);
	}

	public Patient findById(Long id) {
		return this.repository.findById(id).orElseThrow(PatientNotFoundException::new);
	}

}

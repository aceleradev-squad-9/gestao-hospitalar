package gestao.service.patient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestao.exception.patient.PatientAlreadyHasCheckInException;
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

		Long numberOfPatientsInBeds = this.countNumberOfPatientsInBeds(hospital);
		
		if(existsPersonWithCheckInOnHospital(person, hospital)) {
			throw new PatientAlreadyHasCheckInException();
		}
		
		Patient patient = hospital.checkIn(numberOfPatientsInBeds, person);

		return this.repository.save(patient);
	}

	public Patient checkOut(Person person, Hospital hospital) {
		Patient patient = this.getPatient(hospital, person);

		patient.doCheckOut();

		return this.repository.save(patient);
	}
	
	private Boolean existsPersonWithCheckInOnHospital(Person person, Hospital hospital) {
		return this.repository.existsByHospitalAndPersonAndTimeCheckOutIsNull(hospital, person);
	}
	
	private Long countNumberOfPatientsInBeds(Hospital hospital) {
		return this.repository.countByHospitalAndTimeCheckOutIsNull(hospital);
	}

	public List<Patient> getHospitalPatients(Hospital hospital) {

		return this.repository.findAllByHospitalAndTimeCheckOutIsNull(hospital);
	}
	
	public Patient findById(Long id) {
		return this.repository.findById(id).orElseThrow(PatientNotFoundException::new);
	}
	
	public Patient getPatient(Hospital hospital, Person person) {
		return this.repository.findByHospitalAndPersonAndTimeCheckOutIsNull(hospital, person)
				.orElseThrow(PatientNotFoundException::new);
	}

}

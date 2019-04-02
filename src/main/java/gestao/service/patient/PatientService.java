package gestao.service.patient;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestao.exception.patient.PatientNotFoundException;
import gestao.model.hospital.Hospital;
import gestao.model.patient.Patient;
import gestao.model.person.Person;
import gestao.repository.patient.PatientRepository;
import gestao.service.hospital.HospitalService;
import gestao.service.person.PersonService;

/**
 * Classe responsável pela implementação dos serviços relacionados ao paciente.
 * 
 * @author silasalvares
 *
 */
@Service
public class PatientService {
	
	@Autowired
	private HospitalService hospitalService;
	@Autowired
	private PersonService personService;
	@Autowired
	private PatientRepository repository;
	
	public Patient checkIn(Long personId, Long hospitalId) {
		Person person = personService.find(personId);
		Hospital hospital = hospitalService.findById(hospitalId);
		
		Patient patient = Patient.builder()
				.withPerson(person)
				.withHospital(hospital)
				.withTimeCheckIn(LocalDateTime.now())
				.build();
		
		return this.repository.save(patient);
	}
	
	public Patient checkOut(Long patientId) {
		Patient patient = this.repository.findById(patientId).orElseThrow(PatientNotFoundException::new);;
		patient.setTimeCheckOut(LocalDateTime.now());		
		
		return this.repository.save(patient);
	}

}

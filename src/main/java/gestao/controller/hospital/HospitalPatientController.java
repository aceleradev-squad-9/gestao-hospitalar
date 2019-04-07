package gestao.controller.hospital;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gestao.model.hospital.Hospital;
import gestao.model.patient.Patient;
import gestao.model.patient.PatientDto;
import gestao.model.person.Person;
import gestao.service.hospital.HospitalService;
import gestao.service.patient.PatientService;
import gestao.service.person.PersonService;

@RestController
@RequestMapping("/hospital/{hospitalId}/patients")
public class HospitalPatientController {

	@Autowired
	private HospitalService hospitalService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private PatientService patientService;

	@GetMapping
	public List<PatientDto> get(@PathVariable Long hospitalId) {
		Hospital hospital = hospitalService.findById(hospitalId);
		return patientService.getHospitalPatients(hospital).stream().map(Patient::convertToDto)
				.collect(Collectors.toList());
	}
	
	@GetMapping("/{personId}")
	public PatientDto get(@PathVariable Long hospitalId, @PathVariable Long personId) {
		Hospital hospital = hospitalService.findById(hospitalId);
		Person person = personService.findById(personId);
		return patientService.getPatient(hospital, person).convertToDto();
	}
}

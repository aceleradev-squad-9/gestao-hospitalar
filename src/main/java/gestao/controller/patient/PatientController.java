package gestao.controller.patient;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gestao.model.hospital.Hospital;
import gestao.model.patient.Patient;
import gestao.model.patient.PatientDto;
import gestao.service.patient.PatientService;

@RestController
@RequestMapping("/patient")
public class PatientController {
	
	@Autowired
	private PatientService service;
	
	@PostMapping("/{personId}/{hospitalId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PatientDto checkIn(@PathVariable Long personId, @PathVariable Long hospitalId) {
		return this.service.checkIn(personId, hospitalId).convertToDto();
	}
	
	@PutMapping("/{patientId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PatientDto checkOut(@PathVariable Long patientId) {
		return this.service.checkOut(patientId).convertToDto();
	}
	

	@GetMapping("/hospital/{hospitalId}")
	public List<PatientDto> getPatients(@PathVariable Long hospitalId) {
		/*Hospital hospital = hospitalService.findById(hospitalId);
		return patientService.getHospitalPatients(hospital).stream().map(Patient::convertToDto)
				.collect(Collectors.toList());*/
		return null;
	}

}

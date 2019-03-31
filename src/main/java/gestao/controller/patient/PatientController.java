package gestao.controller.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gestao.model.patient.Patient;
import gestao.service.patient.PatientService;

@RestController
@RequestMapping("/patient")
public class PatientController {
	
	@Autowired
	private PatientService service;
	
	@PostMapping("/{personId}/{hospitalId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Patient create(@PathVariable Long personId, @PathVariable Long hospitalId) {
		return this.service.create(personId, hospitalId);
	}

}

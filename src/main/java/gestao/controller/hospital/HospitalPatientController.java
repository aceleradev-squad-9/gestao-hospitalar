package gestao.controller.hospital;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gestao.exception.hospital.NoHospitalAbleToReceivePatientsException;
import gestao.model.hospital.Hospital;
import gestao.model.patient.LocalizationDto;
import gestao.model.patient.Patient;
import gestao.model.patient.PatientDto;
import gestao.model.person.Person;
import gestao.service.hospital.HospitalService;
import gestao.service.patient.PatientService;
import gestao.service.person.PersonService;

@RestController
@RequestMapping("/hospital/")
public class HospitalPatientController {

	@Autowired
	private PatientService patientService;

	@Autowired
	private PersonService personService;

	@Autowired
	private HospitalService hospitalService;

	@PostMapping("/{hospitalId}/checkin/{personId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PatientDto checkIn(@PathVariable("hospitalId") Long hospitalId, @PathVariable("personId") Long personId) {

		Person person = personService.findById(personId);

		Hospital hospital = hospitalService.findById(hospitalId);

		return patientService.checkIn(person, hospital).convertToDto();
	}

	@PutMapping("/patients/{patientId}/checkout")
	public PatientDto checkOut(@PathVariable Long patientId) {
		return patientService.checkOut(patientId).convertToDto();
	}

	@GetMapping(value = "/{hospitalId}/patients", params = { "page", "size" })
	public Page<PatientDto> findAll(@PathVariable Long hospitalId, 
			@RequestParam("page") int page, @RequestParam("size") int size) {
		
		Hospital hospital = hospitalService.findById(hospitalId);
		
		Page<Patient> patients = patientService.findHospitalPatients(hospital, PageRequest.of(page, size));
		
		return patients.map(Patient::convertToDto);
	}

	@GetMapping("/patients/{patientId}")
	public PatientDto find(@PathVariable Long patientId) {
		return patientService.findById(patientId).convertToDto();
	}

	@GetMapping(value="/nearest", params = { "latitude", "longitude" })
	public Hospital findNearestHospitalWithAvailableBeds(@Valid LocalizationDto localizationDto, BindingResult bindingResult) throws BindException {

		if(bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		
		return hospitalService.findNearestHospitals(localizationDto).stream()
				.filter((hospital) -> hospital.hasAvailableBeds(this.patientService.countNumberOfBedsOccupied(hospital)))
				.findFirst().orElseThrow(NoHospitalAbleToReceivePatientsException::new);
	}

}

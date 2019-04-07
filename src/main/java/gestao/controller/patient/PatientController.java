package gestao.controller.patient;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.maps.model.LatLng;

import gestao.model.hospital.Hospital;
import gestao.model.patient.LocalizationDto;
import gestao.model.patient.Patient;
import gestao.service.hospital.HospitalGeoService;
import gestao.service.hospital.HospitalService;
import gestao.service.patient.PatientService;

@RestController
@RequestMapping("/patient")
public class PatientController {
	
	@Autowired
	private PatientService service;
	@Autowired
	private HospitalService hospitalService;
	@Autowired
	private HospitalGeoService hospitalGeoService;
	
	@PostMapping("/{personId}/{hospitalId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Patient checkIn(@PathVariable Long personId, @PathVariable Long hospitalId) {
		return this.service.checkIn(personId, hospitalId);
	}
	
	@PutMapping("/{patientId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Patient checkOut(@PathVariable Long patientId) {
		return this.service.checkOut(patientId);
	}
	
	@GetMapping("/nearest")
	@ResponseStatus(code = HttpStatus.FOUND)
	public List<Hospital> findNearestHospital(@Valid @RequestBody LocalizationDto localizationDto) {
		LatLng latLng = new LatLng(localizationDto.getLatitude(), localizationDto.getLongitude());
		return hospitalGeoService.findNearestHospitalsByLocalization(hospitalService.findAll(), latLng);
	}

}

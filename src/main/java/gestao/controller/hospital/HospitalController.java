package gestao.controller.hospital;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.service.hospital.HospitalService;

@RestController
@RequestMapping("/hospital")
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Hospital createHospital(@Valid @RequestBody HospitalDto hospitalDto) {
		return hospitalService.createHospital(hospitalDto);
	}

	@GetMapping(value = "/{id}")
	public Hospital findById(@PathVariable("id") Long id) {
		return hospitalService.findById(id);
	}

	@GetMapping
	public Iterable<Hospital> findAll() {
		return hospitalService.findAll();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteHospital(@PathVariable("id") Long id) {
		hospitalService.delete(id);
	}

	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Hospital updateHospital(@PathVariable Long id, @Valid @RequestBody HospitalDto hospitalDto) {
		return hospitalService.update(id, hospitalDto);
	}
}

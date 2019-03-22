package gestao.controller.hospital;

import javax.validation.Valid;

import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.service.hospital.HospitalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/hospital")
public class HospitalController {

  @Autowired
  HospitalService hospitalService;

  @PostMapping("")
  @ResponseStatus(code=HttpStatus.CREATED)
  public Hospital createHospital(@Valid @RequestBody HospitalDto hospitalDto){
    return hospitalService.createHospital(hospitalDto);
  }

  @GetMapping(value = "/{id}")
  public Hospital findById(@PathVariable("id") String id) {
    return hospitalService.findById(id);
  }

  @GetMapping
  public List<Hospital> findAll() {
    return hospitalService.findAll();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteHospital(@PathVariable("id") String id){
    hospitalService.delete(id);
  }

  @PutMapping(value="/{id}")
  public Hospital updateHospital(
    @PathVariable String id, 
    @Valid @RequestBody HospitalDto hospitalDto 
  ) {  
    return hospitalService.update(id, hospitalDto);
  }
}
package gestao.controller;

import javax.validation.Valid;

import gestao.model.HospitalDto;
import gestao.service.HospitalService;
import gestao.model.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
package gestao.hospital;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

}
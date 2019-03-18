package gestao.hospital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalService {

  @Autowired
  HospitalRepository hospitalRepository;

  public Hospital createHospital(HospitalDto hospitalDto){
    Hospital newHospital = Hospital.createFromDto(hospitalDto);
    return hospitalRepository.save(newHospital);
  }
}
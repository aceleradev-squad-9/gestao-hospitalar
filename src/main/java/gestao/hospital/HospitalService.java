package gestao.hospital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {

  @Autowired
  HospitalRepository hospitalRepository;

  public Hospital createHospital(HospitalDto hospitalDto){
    Hospital newHospital = Hospital.createFromDto(hospitalDto);
    return hospitalRepository.save(newHospital);
  }

  public Hospital findById(String id) {
    return hospitalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
  }

  public List<Hospital> findAll() {
    return hospitalRepository.findAll();
  }
}
package gestao.service.hospital;

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.repository.hospital.HospitalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

  @Autowired
  HospitalRepository hospitalRepository;

  public Hospital createHospital(HospitalDto hospitalDto){
    Hospital newHospital = Hospital.createFromDto(hospitalDto);
    return hospitalRepository.save(newHospital);
  }

  public Hospital findById(String id) {
    return hospitalRepository.findById(id).orElseThrow(HospitalNotFoundException::new);
  }

  public List<Hospital> findAll() {
    return hospitalRepository.findAll();
  }

  public void delete(String id){
    hospitalRepository.delete(this.findById(id));
  }

  public Hospital update(String hospitalId, HospitalDto hospitalDto){
    Hospital hospital = this.findById(hospitalId);
    hospital.updateFromDto(hospitalDto);
    return hospitalRepository.save(hospital);
  }
}
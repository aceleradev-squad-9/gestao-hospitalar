package gestao.hospital;

public class HospitalService {

  public Hospital createHospital(HospitalDto hospitalDto){
    return Hospital.createFromDto(hospitalDto);
  }
}
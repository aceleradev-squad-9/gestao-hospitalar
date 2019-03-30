package gestao.helper.hospital;

import static java.lang.Math.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.LongStream;
import static java.util.stream.Collectors.toList;

import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;

public class HospitalHelper {
  public static Hospital getAHospitalWithValidProperties(long id){
    
    HospitalDto hospitalDto = new HospitalDto(
      "Hospital " + id, 
      "Descrição do hospital " + id, 
      (int)id, 
      new Address(
        id,
        "Rua do Hospital " + id,
        "Cidade do hospital " + id,
        "Bairro do hospital " + id,
        "Estado do hospital " + id,
        "12345 " + id,
        "2D " + id,
        min(max(11.11 + id, -90),90),
        min(max(2.74 + id, -180),180)
      )  
    );

    final Hospital hospital = Hospital.createFromDto(hospitalDto);

    Field hospitalIdField;
    try {
      hospitalIdField = hospital.getClass().getDeclaredField("id");

      hospitalIdField.setAccessible(true);
	
      hospitalIdField.set(hospital, id);
    } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
      e.printStackTrace();
    } 
    
    return hospital;
  }

  public static List<Hospital> getListOfValidHospitals(int n){
    return LongStream.rangeClosed(1, n)
      .<Hospital>mapToObj(HospitalHelper::getAHospitalWithValidProperties)
      .collect(toList());
  }
}
package gestao.model.hospital;

import org.springframework.data.mongodb.core.mapping.Document;

import gestao.model.address.Address;
import gestao.model.patient.Patient;
import gestao.model.stock.Stock;

import java.util.List;

@Document(collection="hospital")
public class Hospital {
  private String id;

  private String name;

  private String description;

  // capacidade maxima de leitos do hospital
  private Integer beds;

  private List<Patient> patients;

  private Address address;

  private Stock stock;

  private Hospital(){}

  public Hospital( String name, String description){
    this.name = name;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public static Hospital createFromDto(HospitalDto dto){
    Hospital hospital = new Hospital();
    hospital.id = dto.getId();
    hospital.name = dto.getName();
    hospital.description = dto.getDescription();
    return hospital;
  }
}
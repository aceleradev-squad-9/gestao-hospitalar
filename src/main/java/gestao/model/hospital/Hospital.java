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

  private Integer maximumNuberOfBeds;

  private List<Patient> patients;

  private Address address;

  private Stock stock;

  private Hospital(){}

  public Hospital(String name, String description, Integer maximumNuberOfBeds){
    this.name = name;
    this.description = description;
    this.maximumNuberOfBeds = maximumNuberOfBeds;
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

  public Integer getMaximumNuberOfBeds() {
    return maximumNuberOfBeds;
  }

  public Address getAddress() {
    return address;
  }

  public static Hospital createFromDto(HospitalDto dto){
    Hospital hospital = new Hospital();
    hospital.name = dto.getName();
    hospital.description = dto.getDescription();
    hospital.maximumNuberOfBeds = dto.getMaximumNuberOfBeds();
    hospital.address = dto.getAddress();
    return hospital;
  }
}
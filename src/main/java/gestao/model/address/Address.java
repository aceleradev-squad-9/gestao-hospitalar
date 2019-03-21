package gestao.model.address;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Address {
  private String street;

  private String city;

  private String state;

  private String cep;

  private String number;

  private Double latitude;

  private Double longitude;

  public Address(
    String street, 
    String city, 
    String state, 
    String cep, 
    String number, 
    Double latitude, 
    Double longitude 
  ){
    this.street = street;
    this.city = city;
    this.state = state;
    this.cep = cep;
    this.number = number; 
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @NotBlank(message = "Você deve informar a rua.")
  public String getStreet() {
    return street;
  }

  @NotBlank(message = "Você deve informar a cidade.")
  public String getCity() {
    return city;
  }
  
  @NotBlank(message = "Você deve informar o estado.")
  public String getState() {
    return state;
  }

  public String getCep() {
    return cep;
  }

  public String getNumber() {
    return number;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }
}

package gestao.model.address;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Address {
  @NotBlank(message = "Você deve informar a rua.")
  private String street;

  @NotBlank(message = "Você deve informar a cidade.")
  private String city;

  @NotBlank(message = "Você deve informar o estado.")
  private String state;

  @NotBlank(message = "Você deve informar o cep.")
  private String cep;

  @NotBlank(message = "Você deve informar o número.")
  private String number;

  @NotBlank(message = "Você deve informar a latitude.")
  private Double latitude;

  @NotBlank(message = "Você deve informar a longitude.")
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

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }
  
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

package gestao.model.address;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.springframework.data.mongodb.core.mapping.Document;

import gestao.model.converter.DoubleDeserializer;

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

  @NotNull(message = "A latitude não deve ser nula e deve ser um número real.")
  @JsonDeserialize(using = DoubleDeserializer.class)
  private Double latitude;

  @NotNull(message = "A longitude não deve ser nula e deve ser um número real.")
  @JsonDeserialize(using = DoubleDeserializer.class)
  private Double longitude;

  @JsonCreator
  public Address(
    @JsonProperty("street") String street, 
    @JsonProperty("city") String city, 
    @JsonProperty("state") String state, 
    @JsonProperty("cep") String cep, 
    @JsonProperty("number") String number, 
    @JsonProperty("latitude") Double latitude, 
    @JsonProperty("longitude") Double longitude 
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

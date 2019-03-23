package gestao.model.hospital;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gestao.model.converter.IntegerDeserializer;

import gestao.model.address.Address;

public class HospitalDto {

  @NotBlank(message = "O hospital tem que ter nome.")
  private String name;

  @NotBlank(message = "O hospital tem que ter uma descrição.")
  private String description;

  @Min(value = 1, message = "O número de leitos deve ser maior ou igual a 1.")
  @NotNull(message = "O número de leitos não deve ser nulo e deve ser um número inteiro.")
	@JsonDeserialize(using = IntegerDeserializer.class)
  private Integer maximumNumberOfBeds;

  @NotNull(message = "O hospital deve ter um endereço.")
  @Valid
  private Address address;

  @JsonCreator
  public HospitalDto(
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("maximumNumberOfBeds") Integer maximumNumberOfBeds,
    @JsonProperty("address") Address address  
  ){
    this.name = name;
    this.description = description;
    this.maximumNumberOfBeds = maximumNumberOfBeds;
    this.address = address;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Integer getMaximumNumberOfBeds(){
    return maximumNumberOfBeds;
  }

  public Address getAddress(){
    return address;
  }
}
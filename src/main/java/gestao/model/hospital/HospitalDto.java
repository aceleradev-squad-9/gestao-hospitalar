package gestao.model.hospital;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import gestao.model.address.Address;

public class HospitalDto {
  private String id;

  @NotBlank(message = "O hospital tem que ter nome.")
  private String name;

  @NotBlank(message = "O hospital tem que ter uma descrição.")
  private String description;

  @Min(value = 1, message = "O número de leitos deve ser maior ou igual a 1.")
  @NotNull(message = "O número de leitos não deve ser nulo.")
  private Integer maximumNuberOfBeds;

  @NotNull(message = "O hospital deve ter um endereço.")
  @Valid
  private Address address;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Integer getMaximumNuberOfBeds(){
    return maximumNuberOfBeds;
  }

  public Address getAddress(){
    return address;
  }
}
package gestao.model.address;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gestao.util.jackson.DoubleDeserializer;

@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	@Min(value = -90, message = "A latitude deve estar contida no intervalo [-90, 90]")
	@Max(value = +90, message = "A latitude deve estar contida no intervalo [-90, 90]")
	@NotNull(message = "A latitude não deve ser nula e deve ser um número real.")
	@JsonDeserialize(using = DoubleDeserializer.class)
	private Double latitude;

	@Min(value = -180, message = "A longitude deve estar contida no intervalo [-180, 180]")
	@Max(value = +180, message = "A longitude deve estar contida no intervalo [-180, 180]")
	@NotNull(message = "A longitude não deve ser nula e deve ser um número real.")
	@JsonDeserialize(using = DoubleDeserializer.class)
	private Double longitude;

	Address() {
	}
	
	@JsonCreator
	public Address(@JsonProperty("id") Long id, @JsonProperty("street") String street, @JsonProperty("city") String city,
			@JsonProperty("state") String state, @JsonProperty("cep") String cep, @JsonProperty("number") String number,
			@JsonProperty("latitude") Double latitude, @JsonProperty("longitude") Double longitude) {
		this.id = id;
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
	
	public Long getId() {
		return id;
	}
}

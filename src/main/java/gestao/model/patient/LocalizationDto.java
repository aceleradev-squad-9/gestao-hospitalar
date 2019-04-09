package gestao.model.patient;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gestao.util.jackson.DoubleDeserializer;

public class LocalizationDto {

	
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
	
	@JsonCreator
	public LocalizationDto(@JsonProperty("latitude") Double latitude, @JsonProperty("longitude") Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

}

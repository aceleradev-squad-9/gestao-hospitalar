package gestao.model.patient;

import javax.validation.constraints.NotNull;

public class LocalizationDto {
	
	@NotNull(message = "Favor informar a latitude.")
	private Double latitude;
	
	@NotNull(message = "Favor informar a longitue.")
	private Double longitude;

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	

}

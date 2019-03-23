package gestao.model.hospital;

import javax.validation.constraints.NotBlank;

public class HospitalDto {
	private Long id;

	@NotBlank(message = "O hospital tem que ter nome.")
	private String name;

	@NotBlank(message = "O hospital tem que ter uma descrição.")
	private String description;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
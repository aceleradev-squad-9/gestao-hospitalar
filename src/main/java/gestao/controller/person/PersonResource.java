package gestao.controller.person;

import java.time.LocalDate;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import gestao.model.person.Gender;

@JsonPropertyOrder({"id"})
public class PersonResource extends ResourceSupport {

	private Long id;
	
	private String name;

	private String cpf;

	private LocalDate dateOfBirth;

	private Gender gender;

	public PersonResource(Long id, String name, String cpf, LocalDate dateOfBirth, Gender gender) {
		this.id = id;
		this.name = name;
		this.cpf = cpf;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
	}
	
	@JsonProperty(value="id")
	public Long getResourceId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

}

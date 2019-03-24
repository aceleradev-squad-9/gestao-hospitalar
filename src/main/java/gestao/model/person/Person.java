package gestao.model.person;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gestao.util.DateDeserializer;
import gestao.util.DateSerializer;

/**
 * Classe responsável pela representação de uma pessoa.
 * 
 * @author edmilson.santana
 *
 */
@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome é obrigatório.")
	private String name;

	@CPF(message = "O cpf não foi informado ou é inválido.")
	private String cpf;

	@NotNull(message = "A data de nascimento não foi informada ou é inválida.")
	@Past(message = "A data de nascimento deve estar no passado.")
	@JsonDeserialize(using = DateDeserializer.class)
	@JsonSerialize(using = DateSerializer.class)
	private LocalDate dateOfBirth;

	@NotNull(message = "O sexo da pessoa não foi informado ou é inválido.")
	private Gender gender;

	Person() {

	}

	Person(String name, String cpf, LocalDate dateOfBirth, Gender gender) {
		this.name = name;
		this.cpf = cpf;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

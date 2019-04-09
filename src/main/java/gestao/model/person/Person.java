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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gestao.util.jackson.DateDeserializer;
import gestao.util.jackson.DateSerializer;

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
	@JsonProperty(access = Access.READ_ONLY)
	private Long id;

	@NotBlank(message = "A pessoa deve possuir um nome.")
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

	Person(Long id, String name, String cpf, LocalDate dateOfBirth, Gender gender) {
		this.id = id;
		this.name = name;
		this.cpf = cpf;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCpf() {
		return cpf;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}
	
	public void update(Person person) {
		if(person != null) {
			this.name = person.getName();
			this.cpf = person.getCpf();
			this.dateOfBirth = person.getDateOfBirth();
			this.gender = person.getGender();
		}
	}

	public static PersonBuilder builder() {
		return new PersonBuilder();
	}

	public static class PersonBuilder {

		private Long id;

		private String name;

		private String cpf;

		private LocalDate dateOfBirth;

		private Gender gender;

		public PersonBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public PersonBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public PersonBuilder withCpf(String cpf) {
			this.cpf = cpf;
			return this;
		}

		public PersonBuilder withDateOfBirth(LocalDate dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
			return this;
		}

		public PersonBuilder withGender(Gender gender) {
			this.gender = gender;
			return this;
		}

		public Person build() {
			return new Person(id, name, cpf, dateOfBirth, gender);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

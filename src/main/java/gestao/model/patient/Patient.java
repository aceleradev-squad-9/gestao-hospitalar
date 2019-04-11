package gestao.model.patient;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gestao.model.hospital.Hospital;
import gestao.model.person.Person;
import gestao.util.jackson.DateDeserializer;
import gestao.util.jackson.DateSerializer;

@Entity
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Você deve informar os dados da pessoa.")
	@ManyToOne(fetch = FetchType.EAGER)
	private Person person;

	@NotNull(message = "A hora do check-in é obrigatória.")
	@JsonDeserialize(using = DateDeserializer.class)
	@JsonSerialize(using = DateSerializer.class)
	private LocalDateTime timeCheckIn;

	private LocalDateTime timeCheckOut;

	@NotNull(message = "Você deve informar os dados do hospital.")
	@ManyToOne()
	private Hospital hospital;

	Patient() {

	}

	Patient(Long id, Person person, LocalDateTime timeCheckIn, Hospital hospital) {
		this.id = id;
		this.person = person;
		this.timeCheckIn = timeCheckIn;
		this.hospital = hospital;
	}

	public Long getId() {
		return id;
	}

	public Person getPerson() {
		return person;
	}

	public LocalDateTime getTimeCheckIn() {
		return timeCheckIn;
	}

	public LocalDateTime getTimeCheckOut() {
		return timeCheckOut;
	}

	public void doCheckOut() {
		this.timeCheckOut = LocalDateTime.now();
	}
	
	public Boolean hasCheckedOut() {
		return this.getTimeCheckOut() != null;
	}

	public Hospital getHospital() {
		return hospital;
	}
	
	public PatientDto convertToDto() {

		return new PatientDto(
				person.getId(),
				person.getName(), 
				person.getCpf(), 
				person.getDateOfBirth(), 
				person.getGender(),
				this.getTimeCheckIn(),
				this.getTimeCheckOut());
	}

	public static PatientBuilder builder() {
		return new PatientBuilder();
	}

	public static class PatientBuilder {

		private Long id;

		private Person person;

		private LocalDateTime timeCheckIn;

		private Hospital hospital;

		public PatientBuilder withPerson(Person person) {
			this.person = person;
			return this;
		}

		public PatientBuilder withTimeCheckIn(LocalDateTime timeCheckIn) {
			this.timeCheckIn = timeCheckIn;
			return this;
		}

		public PatientBuilder withHospital(Hospital hospital) {
			this.hospital = hospital;
			return this;
		}

		public PatientBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public Patient build() {
			return new Patient(id, person, timeCheckIn, hospital);
		}
	}
}

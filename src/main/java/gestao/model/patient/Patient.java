package gestao.model.patient;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gestao.model.BaseEntity;
import gestao.model.hospital.Hospital;
import gestao.model.person.Person;


@Entity
public class Patient extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "VocÃª deve informar os dados da pessoa.")
	@ManyToOne(fetch=FetchType.EAGER)
	private Person person;

	@NotNull(message = "A hora do check-in Ã© obrigatÃ³ria.")
	private LocalDateTime timeCheckIn;

	private LocalDateTime timeCheckOut;

	@NotNull(message = "VocÃª deve informar os dados do hospital.")
	@ManyToOne()
	private Hospital hospital;
	
	Patient() {
		
	}
	
	Patient(Person person, LocalDateTime timeCheckIn, LocalDateTime timeCheckOut, Hospital hospital) {
		this.person = person;
		this.timeCheckIn = timeCheckIn;
		this.timeCheckOut = timeCheckOut;
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
	
	public void setTimeCheckOut(LocalDateTime timeCheckOut) {
		this.timeCheckOut = timeCheckOut;
	}

	public Hospital getHospitalId() {
		return hospital;
	}
	
	public static PatientBuilder builder() {
		return new PatientBuilder();
	}

	public static class PatientBuilder {
		
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

		public Patient build() {
			return new Patient(person, timeCheckIn, null, hospital);
		}
	}
}

package gestao.model.patient;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import gestao.model.hospital.Hospital;
import gestao.model.person.Person;


@Entity
public class Patient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Os dados da pessoa é obrigatório.")
	@ManyToOne(fetch=FetchType.EAGER)
	private Person person;

	private LocalDateTime timeCheckIn;

	private LocalDateTime timeCheckOut;

	@ManyToOne(fetch=FetchType.LAZY)
	private Hospital hospital;

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

	public Hospital getHospitalId() {
		return hospital;
	}
}

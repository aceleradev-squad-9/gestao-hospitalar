package gestao.model.patient;

import java.time.LocalDate;
import java.time.LocalDateTime;

import gestao.model.person.Gender;

public class PatientDto {

	private String name;

	private String cpf;

	private LocalDate dateOfBirth;

	private Gender gender;

	private LocalDateTime timeCheckIn;

	PatientDto(String name, String cpf, LocalDate dateOfBirth, Gender gender, LocalDateTime timeCheckIn) {
		this.name = name;
		this.cpf = cpf;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.timeCheckIn = timeCheckIn;
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

	public LocalDateTime getTimeCheckIn() {
		return timeCheckIn;
	}

}

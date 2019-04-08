package gestao.model.patient;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gestao.model.person.Gender;
import gestao.util.jackson.DateDeserializer;
import gestao.util.jackson.DateSerializer;
import gestao.util.jackson.DateTimeSerializer;

public class PatientDto {

	private Long id;

	private String name;

	private String cpf;
	
	@JsonDeserialize(using = DateDeserializer.class)
	@JsonSerialize(using = DateSerializer.class)
	private LocalDate dateOfBirth;

	private Gender gender;

	@JsonSerialize(using = DateTimeSerializer.class)
	private LocalDateTime timeCheckIn;
	
	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonInclude(Include.NON_NULL)
	private LocalDateTime timeCheckOut;

	PatientDto(Long id, String name, String cpf, LocalDate dateOfBirth, Gender gender, LocalDateTime timeCheckIn) {
		this.id = id;
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

	public Long getId() {
		return id;
	}

}

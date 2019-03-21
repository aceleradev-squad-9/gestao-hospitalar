package gestao.model.patient;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import gestao.model.pessoa.Pessoa;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document(collection="patient")
public class Patient {
    @Id
    private String id;

    @NotNull(message = "Os dados da pessoa é obrigatório.")
    private Pessoa person;

    private LocalDateTime timeCheckIn;

    private LocalDateTime timeCheckOut;

    private ObjectId hospitalId;

    Patient(Pessoa person, LocalDateTime timeCheckIn, LocalDateTime timeCheckOut, ObjectId hospitalId) {
        this.person = person;
        this.timeCheckIn = timeCheckIn;
        this.timeCheckOut = timeCheckOut;
        this.hospitalId = hospitalId;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Pessoa getPerson() {
        return person;
    }

    public LocalDateTime getTimeCheckIn() {
        return timeCheckIn;
    }

    public LocalDateTime getTimeCheckOut() {
        return timeCheckOut;
    }

    public ObjectId getHospitalId() {
        return hospitalId;
    }
}

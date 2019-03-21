package gestao.model.patient;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import gestao.model.pessoa.Pessoa;

import java.time.LocalDateTime;

@Document(collection="patient")
public class Patient {
    @Id
    private String id;

    private Pessoa person;
    
    private LocalDateTime timeCheckIn;

    private LocalDateTime timeCheckOut;

    private Patient(){}
}

package gestao.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Patient {
    @Id
    private String id;

    private Pessoa person;

    private LocalDateTime timeCheckIn;

    private LocalDateTime timeCheckOut;

    private Patient(){}
}

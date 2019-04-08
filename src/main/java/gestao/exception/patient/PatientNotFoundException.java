package gestao.exception.patient;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String PATIENT_NOT_FOUND_MESSAGE = "O paciente não foi encontrado.";

	public PatientNotFoundException() {
		super(PATIENT_NOT_FOUND_MESSAGE);
	}
}

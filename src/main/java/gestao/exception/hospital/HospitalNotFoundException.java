package gestao.exception.hospital;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND )
public class HospitalNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3212532896361291287L;
	
	private static final String HOSPITAL_NOT_FOUND_MESSAGE = "O hospital não foi encontrado.";

	public HospitalNotFoundException() {
		super(HOSPITAL_NOT_FOUND_MESSAGE);
	}
}

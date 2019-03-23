package gestao.exception.hospital;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class HospitalNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3212532896361291287L;
	private static final String MSG_HOSPITAL_NOT_FOUND = "O Hospital não foi encontrado.";

	public HospitalNotFoundException() {
		super(MSG_HOSPITAL_NOT_FOUND);
	}
}

package gestao.exception.person;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PersonNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4595831616010165515L;
	
	private static final String MSG_PERSON_NOT_FOUND = "A pessoa não foi encontrada.";

	public PersonNotFoundException() {
		super(MSG_PERSON_NOT_FOUND);
	}
}

package gestao.exception.person;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PersonsWithSameCpfException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9173400954573112409L;
	private static final String MSG_PERSONS_WITH_SAME_CPF = "O cpf informado está em uso.";

	public PersonsWithSameCpfException() {
		super(MSG_PERSONS_WITH_SAME_CPF);
	}

}

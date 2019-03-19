package gestao.exception.pessoa;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PessoaNaoEncontradaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4595831616010165515L;
	
	private static final String MSG_PESSOA_NAO_ENCONTRADA = "A pessoa não foi encontrada.";

	public PessoaNaoEncontradaException() {
		super(MSG_PESSOA_NAO_ENCONTRADA);
	}
}

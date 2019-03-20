package gestao.exception.pessoa;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PessoasComCpfIgualException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9173400954573112409L;
	private static final String MSG_PESSOA_COM_CPF_EM_USO = "O cpf informado está em uso.";

	public PessoasComCpfIgualException() {
		super(MSG_PESSOA_COM_CPF_EM_USO);
	}

}

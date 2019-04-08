package gestao.exception.hospital;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NearestHospitalNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7188411147513217808L;
	private static final String NEAREST_HOSPITAL_NOT_FOUND_MESSAGE = "Não existem hospitais próximos disponíveis.";

	public NearestHospitalNotFoundException() {
		super(NEAREST_HOSPITAL_NOT_FOUND_MESSAGE);
	}
}

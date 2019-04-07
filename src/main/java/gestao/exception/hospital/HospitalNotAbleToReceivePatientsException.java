package gestao.exception.hospital;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class HospitalNotAbleToReceivePatientsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4559398859585170673L;
	
	private static final String MSG_HOSPITAL_NOT_ABLE_TO_RECEIVE_PATIENTS = "Não existem leitos disponíveis no hospital.";
	
	public HospitalNotAbleToReceivePatientsException() {
		super(MSG_HOSPITAL_NOT_ABLE_TO_RECEIVE_PATIENTS);
	}

}

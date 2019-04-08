package gestao.exception.hospital;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoHospitalAbleToReceivePatientsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4559398859585170673L;
	
	private static final String MSG_HOSPITAL_NOT_ABLE_TO_RECEIVE_PATIENTS = "Não existem leitos disponíveis para um paciente.";
	
	public NoHospitalAbleToReceivePatientsException() {
		super(MSG_HOSPITAL_NOT_ABLE_TO_RECEIVE_PATIENTS);
	}

}

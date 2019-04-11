package gestao.exception.patient;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class PatientAlreadyHasCheckOutOnHospitalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053953419709495380L;
	private static final String MSG_PATIENT_ALREADY_HAS_CHECK_OUT_ON_HOSPITAL = "Este paciente já fez check-out no hospital.";

	public PatientAlreadyHasCheckOutOnHospitalException() {
		super(MSG_PATIENT_ALREADY_HAS_CHECK_OUT_ON_HOSPITAL);
	}
}

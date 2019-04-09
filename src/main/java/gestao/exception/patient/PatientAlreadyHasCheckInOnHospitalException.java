package gestao.exception.patient;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class PatientAlreadyHasCheckInOnHospitalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6221313273161012072L;
	private static final String MSG_PATIENT_ALREADY_HAS_CHECK_IN_ON_HOSPITAL = "Esta pessoa possui um check-in em aberto no hospital.";

	public PatientAlreadyHasCheckInOnHospitalException() {
		super(MSG_PATIENT_ALREADY_HAS_CHECK_IN_ON_HOSPITAL);
	}
}

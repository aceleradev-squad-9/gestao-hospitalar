package gestao.exception.bloodbank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BloodBankWithSameBloodTypeException extends RuntimeException {
    private static final long serialVersionUID = -4055858459254690539L;

    private static final String BLOODBANK_WITH_SAME_BLOODTYPE = "Banco de sangue já possui este tipo sanguíneo.";

    public BloodBankWithSameBloodTypeException() {
        super(BLOODBANK_WITH_SAME_BLOODTYPE);
    }

}

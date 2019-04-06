package gestao.exception.bloodbank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BloodBankNotFoundException extends RuntimeException{

    private static final long serialVersionUID = -6534945960686398653L;

    private static final String BLOODBANK_NOT_FOUND_MESSAGE = "O banco de sangue não foi encontrado.";

    public BloodBankNotFoundException() {
        super(BLOODBANK_NOT_FOUND_MESSAGE);
    }
}

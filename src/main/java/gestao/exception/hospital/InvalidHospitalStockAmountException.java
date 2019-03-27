package gestao.exception.hospital;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidHospitalStockAmountException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5323353385319154572L;
	private static final String INVALID_HOSPITAL_STOCK_AMOUNT_MESSAGE = "Não foi possível solicitar o produto, o hospital mais próximo possui apenas o suficiente para o próprio hospital.";

	public InvalidHospitalStockAmountException() {
		super(INVALID_HOSPITAL_STOCK_AMOUNT_MESSAGE);
	}
}

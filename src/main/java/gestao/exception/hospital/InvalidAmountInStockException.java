package gestao.exception.hospital;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidAmountInStockException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8057046401052301349L;
	private static final String MSG_INVALID_AMOUNT_IN_STOCK = "A quantidade do produto em estoque não pode ser menor que zero.";
	
	public InvalidAmountInStockException() {
		super(MSG_INVALID_AMOUNT_IN_STOCK);
	}
}

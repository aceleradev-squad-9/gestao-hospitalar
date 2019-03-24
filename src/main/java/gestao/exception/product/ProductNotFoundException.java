package gestao.exception.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8855695625970816797L;
	private static final String MSG_PRODUCT_NOT_FOUND = "O produto não foi encontrado.";

	public ProductNotFoundException() {
		super(MSG_PRODUCT_NOT_FOUND);
	}

}

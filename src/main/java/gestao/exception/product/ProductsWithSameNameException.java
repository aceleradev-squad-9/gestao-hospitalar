package gestao.exception.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ProductsWithSameNameException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4670913466885157523L;
	private static final String MSG_PRODUCTS_WITH_SAME_NAME = "Já existe um produto cadastrado com o mesmo nome.";

	public ProductsWithSameNameException() {
		super(MSG_PRODUCTS_WITH_SAME_NAME);
	}
}

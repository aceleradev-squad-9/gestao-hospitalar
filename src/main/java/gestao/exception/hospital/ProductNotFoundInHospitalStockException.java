package gestao.exception.hospital;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ProductNotFoundInHospitalStockException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2278453092225192960L;
	private static final String MSG_PRODUCT_NOT_FOUND_IN_HOSPITAL_STOCK = "Não foi possível encontrar o produto no estoque do hospital.";
	
	public ProductNotFoundInHospitalStockException() {
		super(MSG_PRODUCT_NOT_FOUND_IN_HOSPITAL_STOCK);
	}
}

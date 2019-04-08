package gestao.exception.hospital;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoHospitalAbleToTransferProductException extends RuntimeException {

	private static final long serialVersionUID = -7187717322693216670L;
	private static final String NO_PRODUCT_ABLE_TO_TRANSFER_PRODUCT = "Não foi possível encontrar um hospital para transferir o produto.";
	
	public NoHospitalAbleToTransferProductException() {
		super(NO_PRODUCT_ABLE_TO_TRANSFER_PRODUCT);
	}
}

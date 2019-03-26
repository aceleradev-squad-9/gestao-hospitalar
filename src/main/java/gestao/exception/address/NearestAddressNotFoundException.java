package gestao.exception.address;

public class NearestAddressNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7572987548468052629L;
	private static final String NEAREST_ADDRESS_NOT_FOUND_MESSAGE = "Não foi possível localizar o endereço mais próximo.";

	public NearestAddressNotFoundException() {
		super(NEAREST_ADDRESS_NOT_FOUND_MESSAGE);
	}
}

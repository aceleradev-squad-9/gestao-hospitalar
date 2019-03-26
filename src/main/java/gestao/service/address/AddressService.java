package gestao.service.address;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestao.exception.address.NearestAddressNotFoundException;
import gestao.model.address.Address;

/**
 * Classe responsável pela implementação dos serviços relacionados a um
 * endereço.
 * 
 * @author edmilson.santana
 *
 */
@Service
public class AddressService {

	@Autowired
	private GeoApi geoApi;

	public Address findNearestAddress(Address origin, List<Address> destinations) {

		List<Long> distances = geoApi.getDistances(origin.getFormattedAddress(),
				destinations.stream().map(Address::getFormattedAddress).toArray(String[]::new));

		Integer indexOfMin = IntStream.range(0, distances.size()).boxed().min(Comparator.comparingLong(distances::get))
				.orElseThrow(() -> new NearestAddressNotFoundException());

		return destinations.get(indexOfMin);

	}
}

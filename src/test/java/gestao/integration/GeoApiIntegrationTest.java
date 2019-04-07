package gestao.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.maps.model.LatLng;

import gestao.model.address.Address;
import gestao.util.geo.GeoApi;

@SpringBootTest
public class GeoApiIntegrationTest {

	@Autowired
	private GeoApi geoApi;

	@Test
	@DisplayName("Deve retornar a menor distância entre endereços de origem e destino.")
	public void shouldReturnMinorDistanceBetweenAddress() {
		// R. Mário Campelo, 700 - Várzea, Recife - PE, 50740-540
		Address origin = new Address(1L, "R. Mário Campelo", "Recife", "Várzea", "PE", "50740-540", "700", -8.057940,
				-34.958440);
		// Av. Prof. Moraes Rego, 1235 - Cidade Universitária, Recife - PE, 50670-901
		Address destinationA = new Address(1L, "Av. Prof. Moraes Rego", "Recife", "Cidade Universitária", "PE",
				"50670-901", "1235", -8.057940, -34.958440);
		// Av. Prof. Luís Freire, 500 - Cidade Universitária, Recife - PE, 50740-540
		Address destinationB = new Address(2L, "Av. Prof. Luís Freire", "Recife", "Cidade Universitária", "PE",
				"50740-540", "500", -8.057780, -34.952259);

		List<Address> destinations = new ArrayList<>();
		destinations.add(destinationA);
		destinations.add(destinationB);

		Integer indexOfMin = this.getIndexOfMinorDistance(origin.getFormattedAddress(),
				destinations.stream().map(Address::getFormattedAddress).toArray(String[]::new));

		assertTrue(indexOfMin > 0);
		assertEquals(destinationB.getFormattedAddress(), destinations.get(indexOfMin).getFormattedAddress());
	}

	@Test
	@DisplayName("Deve retornar a menor distância entre endereços de origem e destino utilizando latitude e longitude na origem.")
	public void shouldReturnMinorDistanceBetweenAddressUsingLatLng() {

		LatLng origin = new LatLng(-8.053430, -34.959700);

		Address destinationA = new Address(1L, "Av. Prof. Moraes Rego", "Recife", "Cidade Universitária", "PE",
				"50670-901", "1235", -8.057940, -34.958440);

		Address destinationB = new Address(2L, "Av. Prof. Luís Freire", "Recife", "Cidade Universitária", "PE",
				"50740-540", "500", -8.057780, -34.952259);

		List<Address> destinations = new ArrayList<>();
		destinations.add(destinationA);
		destinations.add(destinationB);

		Integer indexOfMin = this.getIndexOfMinorDistance(origin,
				destinations.stream().map(Address::getFormattedAddress).toArray(String[]::new));

		assertTrue(indexOfMin > 0);
		assertEquals(destinationB.getFormattedAddress(), destinations.get(indexOfMin).getFormattedAddress());
	}

	private Integer getIndexOfMinorDistance(LatLng origin, String[] destinations) {
		List<Long> distances = geoApi.getDistances(origin, destinations);
		return IntStream.range(0, distances.size()).boxed().min(Comparator.comparingLong(distances::get)).orElse(-1);
	}

	private Integer getIndexOfMinorDistance(String origin, String[] destinations) {
		List<Long> distances = geoApi.getDistances(origin, destinations);
		return IntStream.range(0, distances.size()).boxed().min(Comparator.comparingLong(distances::get)).orElse(-1);
	}
}
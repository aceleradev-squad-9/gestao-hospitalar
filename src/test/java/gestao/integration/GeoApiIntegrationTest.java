package gestao.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gestao.model.address.Address;
import gestao.util.geo.GeoApi;

@SpringBootTest
public class GeoApiIntegrationTest {

	@Autowired
	private GeoApi geoApi;

	@Test
	@DisplayName("Deve retornar a menor distância entre endereços de origem e destino.")
	public void deveRetornarMenorDistanciaEntreEnderecos() {
		// R. Mário Campelo, 700 - Várzea, Recife - PE, 5074
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

		Integer indexOfMin = geoApi.getIndexOfMinorDistance(origin.getFormattedAddress(),
				destinations.stream().map(Address::getFormattedAddress).toArray(String[]::new));

		assertTrue(indexOfMin > 0);
		assertEquals(destinationB.getFormattedAddress(), destinations.get(indexOfMin).getFormattedAddress());
	}
}
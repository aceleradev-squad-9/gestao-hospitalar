package gestao.service.address;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;

/**
 * Classe responsável pelas requisições com as APIs do Google Geo.
 * 
 * @author edmilson.santana
 *
 */
@Component
public class GeoApi {

	private static final Logger LOG = LogManager.getLogger(GeoApi.class);

	@Value("${maps.apiKey}")
	private String mapsApiKey;

	private DistanceMatrixApiRequest getDistanceMatrixRequest() {
		GeoApiContext context = new GeoApiContext.Builder().apiKey(mapsApiKey).build();
		return new DistanceMatrixApiRequest(context);
	}

	public List<Long> getDistances(String origin, String[] destinations) {
		List<Long> distances = new ArrayList<>();

		try {
			DistanceMatrixApiRequest request = this.getDistanceMatrixRequest();
			DistanceMatrix distanceMatrix = request.origins(origin)
					.destinations(destinations).await();
			distances = this.getDistances(distanceMatrix);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return distances;
	}

	private List<Long> getDistances(DistanceMatrix distanceMatrix) {

		return Stream.of(distanceMatrix).flatMap((matrix) -> Stream.of(matrix.rows))
				.flatMap((row) -> Stream.of(row.elements)).map((element) -> element.distance)
				.map((distance) -> distance.inMeters).collect(Collectors.toList());
	}
}

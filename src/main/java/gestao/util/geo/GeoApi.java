package gestao.util.geo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import gestao.exception.hospital.NoHospitalAbleToTransferProductException;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElementStatus;

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

	public List<Long> getDistances(String dest, String[] origins) {
		List<Long> distances = new ArrayList<>();

		try {
			DistanceMatrixApiRequest request = this.getDistanceMatrixRequest();
			DistanceMatrix distanceMatrix = request.origins(origins).destinations(dest).await();
			distances = this.getDistances(distanceMatrix);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return distances;
	}

	private List<Long> getDistances(DistanceMatrix distanceMatrix) {

		return Stream.of(distanceMatrix)
			.flatMap((matrix) -> Stream.of(matrix.rows))
			.flatMap((row) -> Stream.of(row.elements))
			.map((element) -> {
				if(DistanceMatrixElementStatus.OK.equals(element.status)){
					return element.distance.inMeters;
				}
				return Long.MAX_VALUE;
			})
			.collect(Collectors.toList());
	}
}

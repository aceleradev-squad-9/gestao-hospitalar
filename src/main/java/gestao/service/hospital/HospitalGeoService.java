package gestao.service.hospital;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import gestao.exception.hospital.NearestHospitalNotFoundException;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.util.geo.GeoApi;

@Service
public class HospitalGeoService {

	@Autowired
	private GeoApi geoApi;

	@Cacheable(value = "nearestHospitals")
	public List<Hospital> findNearestHospitals(List<Hospital> hospitals, Address origin) {
		String[] destinations = this.getHospitalsFormattedAddresses(hospitals);

		List<Long> distances = geoApi.getDistances(origin.getFormattedAddress(), destinations);

		if (distances.size() == 0) {
			throw new NearestHospitalNotFoundException();
		}
		
		return this.getHospitalsSortedByDistance(hospitals, distances);
	}

	public List<Hospital> getHospitalsSortedByDistance(List<Hospital> hospitals, List<Long> distances) {
		return IntStream.range(0, distances.size()).boxed()
				.map((i) -> new AbstractMap.SimpleEntry<>(hospitals.get(i), distances.get(i)))
				.sorted(Comparator.comparing(AbstractMap.SimpleEntry::getValue)).map(AbstractMap.SimpleEntry::getKey)
				.collect(Collectors.toList());
	}

	public String[] getHospitalsFormattedAddresses(List<Hospital> hospitals) {
		return hospitals.stream().map(hospital -> hospital.getAddress().getFormattedAddress()).toArray(String[]::new);
	}

	public List<Hospital> sortHospitalsByDistanceFromAnOrigin(List<Hospital> hospitals, Address origin) {
		List<Long> distances = geoApi.getDistances(origin.getFormattedAddress(),
				this.getHospitalsFormattedAddresses(hospitals));

		if (distances.size() == 0) {
			throw new NearestHospitalNotFoundException();
		}

		ArrayList<Long> arrayOfDistances = new ArrayList<>(distances);
		ArrayList<Hospital> arrayOfHospitals = new ArrayList<>(hospitals);

		return IntStream.range(0, hospitals.size()).boxed().sorted(Comparator.comparing(arrayOfDistances::get))
				.<Hospital>map(i -> arrayOfHospitals.get(i)).collect(Collectors.toList());
	}
}

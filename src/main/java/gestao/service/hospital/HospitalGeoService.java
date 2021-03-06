package gestao.service.hospital;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.maps.model.LatLng;

import gestao.exception.hospital.NearestHospitalNotFoundException;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.patient.LocalizationDto;
import gestao.util.geo.GeoApi;

@Service
public class HospitalGeoService {

	@Autowired
	private GeoApi geoApi;

	public List<Hospital> findNearestHospitals(List<Hospital> hospitals, Address dest) {
		String[] origins = this.getHospitalsFormattedAddresses(hospitals);

		List<Long> distances = geoApi.getDistances(dest.getFormattedAddress(), origins);

		this.validateHospitalsDistances(distances);

		return this.getHospitalsSortedByDistance(hospitals, distances);
	}

	public List<Hospital> findNearestHospitals(List<Hospital> hospitals, LocalizationDto localizationDto) {
		String[] destinations = this.getHospitalsFormattedAddresses(hospitals);

		List<Long> distances = geoApi
				.getDistances(new LatLng(localizationDto.getLatitude(), 
						localizationDto.getLongitude()), destinations);

		this.validateHospitalsDistances(distances);

		return this.getHospitalsSortedByDistance(hospitals, distances);
	}

	private void validateHospitalsDistances(List<Long> distances) {
		if (distances.size() == 0) {
			throw new NearestHospitalNotFoundException();
		}
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
}

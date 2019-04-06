package gestao.service.hospital;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import gestao.exception.hospital.NearestHospitalNotFoundException;
import gestao.helper.hospital.HospitalHelper;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.util.geo.GeoApi;

@SpringBootTest
public class HospitalGeoServiceTest {

  @MockBean
  private GeoApi mockedGeoApiService;

  @Autowired
  private HospitalGeoService hospitalGeoService;

  @Test
  @DisplayName("Deve construir uma lista com N endereços formatados, sendo que o endereço i da lista deve corresponder ao endereço do hospital i.")
  public void shouldBuildAListOfNStringsWithFormattedAddressesFromNHospitals() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    List<Hospital> hospitals = HospitalHelper.getListOfValidHospitals(3);
     
    String[] formattedAddresses = hospitalGeoService.getHospitalsFormattedAddresses(hospitals);
    assertArrayEquals(
      hospitals.stream()
        .map(h -> h.getAddress().getFormattedAddress())
        .toArray(String[]::new), 
      formattedAddresses
    );
  }

  @Test
  @DisplayName("Deve ordenar uma lista de hospitais de acordo com uma lista de distâncias.")
  public void shouldSortAListOfHospitalsAcordingToTheirDistances(){
    List<Hospital> hospitals = HospitalHelper.getListOfValidHospitals(10);

    List<Long> distanceToSomePlace1 = Arrays.asList(
      234L, //pos: 5
      123L, //pos: 3
      21L,  //pos: 0
      55L,  //pos: 2
      500L, //pos: 7
      321L, //pos: 6
      21L,  //pos: 1
      500L, //pos: 8
      233L, //pos: 4
      600L  //pos: 9
    );

    List<Hospital> sortedHospitals1 = Arrays.asList(
      hospitals.get(2),
      hospitals.get(6),
      hospitals.get(3),
      hospitals.get(1),
      hospitals.get(8),
      hospitals.get(0),
      hospitals.get(5),
      hospitals.get(4),
      hospitals.get(7),
      hospitals.get(9)
    );

    assertArrayEquals(
      sortedHospitals1.stream().toArray(), 
      hospitalGeoService.getHospitalsSortedByDistance(hospitals, distanceToSomePlace1)
        .toArray()
    );
  }

  @Test
  @DisplayName("Deve ordenar uma lista de hospitais de acordo com a distância para um endereço.")
  public void shouldSortAListOfHOspitalsAcordingToTheDistanceToAnAddress(){
    List<Hospital> hospitals = HospitalHelper.getListOfValidHospitals(10);

    List<Long> distanceToTheOrigin = Arrays.asList(
      234L, //pos: 5
      123L, //pos: 3
      21L,  //pos: 0
      55L,  //pos: 2
      500L, //pos: 7
      321L, //pos: 6
      21L,  //pos: 1
      500L, //pos: 8
      233L, //pos: 4
      600L  //pos: 9
    );

    List<Hospital> sortedHospitals1 = Arrays.asList(
      hospitals.get(2),
      hospitals.get(6),
      hospitals.get(3),
      hospitals.get(1),
      hospitals.get(8),
      hospitals.get(0),
      hospitals.get(5),
      hospitals.get(4),
      hospitals.get(7),
      hospitals.get(9)
    );

    when(
      mockedGeoApiService.getDistances(
        isA(String.class), 
        isA(String[].class)
      )
    ).thenReturn(distanceToTheOrigin);

    final Hospital origin = HospitalHelper.getAHospitalWithValidProperties(11);
    assertArrayEquals(
      sortedHospitals1.stream().toArray(), 
      hospitalGeoService.findNearestHospitals(hospitals, origin.getAddress())
        .stream()
        .toArray()
    );

    assertArrayEquals(
      sortedHospitals1.stream().toArray(), 
      hospitalGeoService.sortHospitalsByDistanceFromAnOrigin(hospitals, origin.getAddress())
        .stream()
        .toArray()
    );
  }

  @Test
  @DisplayName("Deve jogar uma NearestHospitalNotFoundException quando não existe nenhum hospital mais próximo.")
  public void shouldThrowAnExceptionWhenThereIsNoNearestHospital(){
    when(
      mockedGeoApiService.getDistances(
        isA(String.class), 
        isA(String[].class)
      )
    ).thenReturn(new ArrayList<>());

    List<Hospital> emptyHospitalList = Arrays.asList();
    Address origin = HospitalHelper.getAHospitalWithValidProperties(1L).getAddress();
    assertThrows(
      NearestHospitalNotFoundException.class, 
      () -> {
        hospitalGeoService.findNearestHospitals(
          emptyHospitalList, 
          origin
        );
      }
    );

    assertThrows(
      NearestHospitalNotFoundException.class, 
      () -> {
        hospitalGeoService.sortHospitalsByDistanceFromAnOrigin(
          emptyHospitalList, 
          origin
        );
      }
    );
  }
}
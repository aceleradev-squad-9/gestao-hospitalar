package gestao.service.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.repository.hospital.HospitalRepository;

@SpringBootTest
public class HospitalServiceTest {

  @Mock
  HospitalRepository mockedHospitalRepository;

  @InjectMocks
  @Autowired
  HospitalService hospitalService;

  @Test
  @DisplayName("Deve enviar o HospitalDto correto para HospitalRepository.save e retornar o Hospital correto.")
  public void shouldSendTheCorrectHospitalDtoToHospitalRepositoryAndReturnTheCorrectHospital(){
    final HospitalDto hospitalDto = new HospitalDto(
      "Hospital 1", 
      "Descrição do hospital 1", 
      123, 
      new Address(
        null,
        "Rua do Hospital 1",
        "Cidade do hospital 1",
        "Bairro do hospital 1",
        "Estado do hospital 1",
        "12345678",
        "23D",
        34.234,
        12.121
      )  
    );

    final Hospital hospital = Hospital.createFromDto(hospitalDto);
    when(mockedHospitalRepository.save(hospital)).thenReturn(hospital);

    final Hospital hospitalCreatedByService = hospitalService.createHospital(hospitalDto);

    assertEquals(
      hospital.getName(),
      hospitalCreatedByService.getName()
    );

    assertEquals(
      hospital.getDescription(),
      hospitalCreatedByService.getDescription()
    );

    assertEquals(
      hospital.getMaximumNumberOfBeds(),
      hospitalCreatedByService.getMaximumNumberOfBeds()
    );

    final Address address = hospital.getAddress();
    final Address createdAddress = hospitalCreatedByService.getAddress();

    assertEquals(
      address.getStreet(),
      createdAddress.getStreet()
    );

    assertEquals(
      address.getDistrict(),
      createdAddress.getDistrict()
    );

    assertEquals(
      address.getCity(),
      createdAddress.getCity()
    );

    assertEquals(
      address.getState(),
      createdAddress.getState()
    );

    assertEquals(
      address.getCep(),
      createdAddress.getCep()
    );

    assertEquals(
      address.getNumber(),
      createdAddress.getNumber()
    );

    assertEquals(
      address.getLatitude(),
      createdAddress.getLatitude()
    );

    assertEquals(
      address.getLongitude(),
      createdAddress.getLongitude()
    );
  }
}
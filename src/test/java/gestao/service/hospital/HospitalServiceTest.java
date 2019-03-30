package gestao.service.hospital;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.repository.hospital.HospitalRepository;

@SpringBootTest
public class HospitalServiceTest {

  @MockBean
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

  @Test
  @DisplayName("Deve retornar o hospital se encontrado senão jogar HospitalNotFoundException.")
  public void shouldReturnTheFoundHospitalOrThrowHospitalNotFoundException(){
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

    final long EXISTING_ID = 1L;
    Optional<Hospital> optionalHospital = Optional.of(Hospital.createFromDto(hospitalDto));
    when(mockedHospitalRepository.findById(EXISTING_ID))
      .thenReturn(optionalHospital);

    assertDoesNotThrow(
      () -> {
        hospitalService.findById(EXISTING_ID);
      }
    );


    final Hospital foundHospital = hospitalService.findById(EXISTING_ID);

    assertEquals(
      hospitalDto.getName(),
      foundHospital.getName()
    );

    assertEquals(
      hospitalDto.getDescription(),
      foundHospital.getDescription()
    );

    assertEquals(
      hospitalDto.getMaximumNumberOfBeds(),
      foundHospital.getMaximumNumberOfBeds()
    );

    final Address address = hospitalDto.getAddress();
    final Address foundAddress = foundHospital.getAddress();

    assertEquals(
      address.getStreet(),
      foundAddress.getStreet()
    );

    assertEquals(
      address.getDistrict(),
      foundAddress.getDistrict()
    );

    assertEquals(
      address.getCity(),
      foundAddress.getCity()
    );

    assertEquals(
      address.getState(),
      foundAddress.getState()
    );

    assertEquals(
      address.getCep(),
      foundAddress.getCep()
    );

    assertEquals(
      address.getNumber(),
      foundAddress.getNumber()
    );

    assertEquals(
      address.getLatitude(),
      foundAddress.getLatitude()
    );

    assertEquals(
      address.getLongitude(),
      foundAddress.getLongitude()
    );


    final Long NOT_EXISTING_ID = 2L;
    when(mockedHospitalRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

    assertThrows(
      HospitalNotFoundException.class, 
      () -> hospitalService.findById(NOT_EXISTING_ID)
    );
  }

  @Test
  @DisplayName("Deve retornar uma lista com os hospitais recebidos de HospitalRepository.")
  public void shouldReturnAListWithAllHospitalsUsingHospitalRepository(){
    final HospitalDto hospitalDto1 = new HospitalDto(
      "Hospital 1", 
      "Descrição do hospital 1", 
      123, 
      new Address(
        1L,
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

    final HospitalDto hospitalDto2 = new HospitalDto(
      "Hospital 2", 
      "Descrição do hospital 2", 
      121, 
      new Address(
        2L,
        "Rua do Hospital 2",
        "Cidade do hospital 2",
        "Bairro do hospital 2",
        "Estado do hospital 2",
        "23456789",
        "41D",
        11.234,
        13.121
      )  
    );

    final List<Hospital> allHospitals = Arrays.asList(
      Hospital.createFromDto(hospitalDto1),
      Hospital.createFromDto(hospitalDto2)
    );

    when(mockedHospitalRepository.findAll()).thenReturn(allHospitals);

    final List<Hospital> allHospitalsFromHospitalService = hospitalService.findAll();

    verify(mockedHospitalRepository, times(1)).findAll();

    assertEquals(allHospitalsFromHospitalService, allHospitals);
    assertEquals(allHospitalsFromHospitalService.size(), allHospitals.size());
    
    for(int i = 0; i < allHospitals.size(); i++){
      assertEquals(
        allHospitalsFromHospitalService.get(i),
        allHospitals.get(i)
      );
    }
  }

  @Test
  @DisplayName("Deve remover o hospital correto encontrado por HospitalService.findById.")
  public void shouldDeleteTheCorrectHospital(){
    final HospitalDto hospitalDto = new HospitalDto(
      "Hospital 1", 
      "Descrição do hospital 1", 
      123, 
      new Address(
        1L,
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

    final Long EXISTING_ID = 1L;

    final Hospital hospitalTobeDeleted = Hospital.createFromDto(hospitalDto);
    when(mockedHospitalRepository.findById(EXISTING_ID)).thenReturn(
      Optional.of(hospitalTobeDeleted)
    );

    hospitalService.delete(EXISTING_ID);

    verify(mockedHospitalRepository, times(1)).findById(EXISTING_ID);

    verify(mockedHospitalRepository, times(1)).delete(hospitalTobeDeleted);
  }

  @Test
  @DisplayName("Deve jogar HospitalNotFoundException quando nenhum hospital possui o id enviado para HospitalService.delete")
  public void shouldThrowHospitalNotFoundException(){
    final Long NOT_EXISTING_ID = 1L;
    when(mockedHospitalRepository.findById(NOT_EXISTING_ID)).thenThrow(
      new HospitalNotFoundException()
    );

    assertThrows(
      HospitalNotFoundException.class, 
      () -> hospitalService.delete(NOT_EXISTING_ID)
    );
  }
}
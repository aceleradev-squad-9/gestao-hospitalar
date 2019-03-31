package gestao.controller.hospital;

import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.service.hospital.HospitalService;

@WebMvcTest(controllers = HospitalController.class)
public class HospitalControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private HospitalService mockedHospitalService;

  @InjectMocks
  @Autowired
  private HospitalController hospitalController;

  @Test
  @DisplayName("Deve receber o body em formato json com as info do hospital criado e também deve receber http status code 201.")
  public void shouldReceive201HttpStatusCodeWithTheCorrectJsonBody() throws Exception {
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

    final Hospital hospital = Hospital.createFromDto(hospitalDto);
    when(
      mockedHospitalService.createHospital(isA(HospitalDto.class))
    ).thenReturn(hospital);

    final ObjectMapper objectMapper = new ObjectMapper();
    final String hospitalDtoJson = objectMapper.writeValueAsString(hospitalDto);
    MvcResult mvcResult = mvc.perform(
      post("/hospital")
        .contentType(MediaType.APPLICATION_JSON)
        .content(hospitalDtoJson)
        .accept(MediaType.APPLICATION_JSON)
    ) 
    .andExpect(status().isCreated())
    .andReturn();    
    
    final String hospitalJson = objectMapper.writeValueAsString(hospital);
    assertEquals(
      hospitalJson,
      mvcResult.getResponse().getContentAsString()
    );
  }

  @Test
  @DisplayName("Verificar se o controlador está passando a instância correta de HospitalDto para o método HospitalService.createHospital quando utilizado o método hospitalController.createHospital.")
  public void shouldSendTheCorrectInstanceOfHospitalDtoToCreateHospitalMethod(){
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

    when(
      mockedHospitalService.createHospital(hospitalDto)
    ).thenReturn(Hospital.createFromDto(hospitalDto));

    hospitalController.createHospital(hospitalDto);

    verify(mockedHospitalService, times(1)).createHospital(hospitalDto);
  }

  @Test
  @DisplayName("Deve receber um status code 400 as mensagens de validação para os campos que não permitem nulo")
  public void shouldReceiveTheValidationMessagesForFieldsWhoMustNotBeNull() throws Exception {
    mvc.perform(
      post("/hospital")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}")
        .accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isBadRequest())
    .andExpect(
      jsonPath("$.errors.name[0]", is("O hospital tem que ter nome."))
    )
    .andExpect(
      jsonPath("$.errors.description[0]", is("O hospital tem que ter uma descrição."))
    )
    .andExpect(
      jsonPath("$.errors.maximumNumberOfBeds[0]", is("O número de leitos não deve ser nulo e deve ser um número inteiro."))
    )
    .andExpect(
      jsonPath("$.errors.address[0]", is("O hospital deve ter um endereço."))
    )
    .andReturn();
  }

  @Test
  @DisplayName("Deve receber uma mensagem de validação indicando que o número de leitos inseridos é menor que o permitido.")
  public void shouldReceiveTheCorrectValidationMessagesForTheNumberOfBeds() throws Exception {
    final HospitalDto hospitalDto = new HospitalDto(
      "Hospital 1", 
      "Descrição do hospital 1", 
      0, 
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
    final ObjectMapper objectMapper = new ObjectMapper();
    final String hospitalDtoJson = objectMapper.writeValueAsString(hospitalDto);
    mvc.perform(
      post("/hospital")
        .contentType(MediaType.APPLICATION_JSON)
        .content(hospitalDtoJson)
        .accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isBadRequest())
    .andExpect(
      jsonPath("$.errors.maximumNumberOfBeds[0]", is("O número de leitos deve ser maior ou igual a 1."))
    )
    .andReturn();
  }

  @Test
  @DisplayName("Deve receber as mensagens de valição corretas para um endereço inválido de hospital.")
  public void shouldReceiveTheCorrectValidationMessagesForAnInvalidHospitalAddress() throws Exception {
    final HospitalDto hospitalDto = new HospitalDto(
      "Hospital 1", 
      "Descrição do hospital 1", 
      0, 
      new Address(
        1L,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
      )  
    );
    final ObjectMapper objectMapper = new ObjectMapper();
    final String hospitalDtoJson = objectMapper.writeValueAsString(hospitalDto);
    mvc.perform(
      post("/hospital")
        .contentType(MediaType.APPLICATION_JSON)
        .content(hospitalDtoJson)
        .accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isBadRequest())
    .andExpect(
      jsonPath(
        "$.errors.['address.street'][0]", 
        is("Você deve informar a rua.")
      )
    )
    .andExpect(
      jsonPath(
        "$.errors.['address.city'][0]", 
        is("Você deve informar a cidade.")
      )
    )
    .andExpect(
      jsonPath(
        "$.errors.['address.district'][0]", 
        is("Você deve informar o bairro.")
      )
    )
    .andExpect(
      jsonPath(
        "$.errors.['address.state'][0]", 
        is("Você deve informar o estado.")
      )
    )
    .andExpect(
      jsonPath(
        "$.errors.['address.cep'][0]", 
        is("Você deve informar o cep.")
      )
    )
    .andExpect(
      jsonPath(
        "$.errors.['address.number'][0]", 
        is("Você deve informar o número.")
      )
    )
    .andReturn();
  }

  @Test
  @DisplayName("Deve receber o hospital buscado a partir do id")
  public void shouldReceiveAHospitalObjectWhichHasTheId() throws Exception{
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

    final Hospital hospital = Hospital.createFromDto(hospitalDto);
    final Long HOSPITAL_ID = 1L;
    when(mockedHospitalService.findById(HOSPITAL_ID)).thenReturn(hospital);

    MvcResult result = mvc.perform(
      get("/hospital/" + HOSPITAL_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isOk())
    .andReturn();

    final ObjectMapper objectMapper = new ObjectMapper();
    final String hospitalJson = objectMapper.writeValueAsString(hospital);
    assertEquals(
      hospitalJson, 
      result.getResponse().getContentAsString()
    );
  }

  @Test
  @DisplayName("Deve receber status code 404 quando o serviço não encontra um hospital..")
  public void shouldReceiveHttpStatusCode404WhenTheHospitalServiceDoesNotFindTheHospital() throws Exception{
    when(mockedHospitalService.findById(any())).thenThrow(new HospitalNotFoundException());

    MvcResult result = mvc.perform(
      get("/hospital/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isNotFound())
    .andReturn();

    assertEquals(
      "", 
      result.getResponse().getContentAsString()
    );
  }

  @Test
  @DisplayName("Deve receber uma lista com todos os hospitais montada pelo HospitalService")
  public void shouldReceiveAListWithAllTheHospitalsFromHospitalService() throws Exception{
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

    List<Hospital> allHospitals = Arrays.asList(
      Hospital.createFromDto(hospitalDto1),
      Hospital.createFromDto(hospitalDto2)
    );

    Page<Hospital> hospitalPage = new PageImpl<Hospital>(allHospitals);
    when(
      mockedHospitalService.findAll(isA(PageRequest.class))
    ).thenReturn(hospitalPage);

    MvcResult result = mvc.perform(
      get("/hospital?page=0&size=" + allHospitals.size())
        .accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isOk())
    .andReturn();

    final ObjectMapper objectMapper = new ObjectMapper();
    final String hospitalJson = objectMapper.writeValueAsString(hospitalPage);
    assertEquals(
      hospitalJson, 
      result.getResponse().getContentAsString()
    );

    final Page<Hospital> allHospitalsFromHospitalController = hospitalController.findAll(
      1,
      allHospitals.size()
    );

    verify(mockedHospitalService, times(2)).findAll(isA(PageRequest.class));
    assertArrayEquals(
      allHospitalsFromHospitalController.get().toArray(),
      allHospitals.stream().toArray()
    );
  }

  @Test
  @DisplayName("Deve receber http status code 204 quando recebe um request para remover um hospital com id existente")
  public void shouldReceiveHttpStatusCode204AfterDeletingAnExistingHospital() throws Exception{
    final Long EXISTING_ID = 1L;

    mvc.perform(
      delete("/hospital/" + EXISTING_ID).accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isNoContent());

    verify(mockedHospitalService, times(1)).delete(EXISTING_ID);
  }

  @Test
  @DisplayName("Deve receber http status code 404 após tentar remover um hospital inexistente.")
  public void shouldReceiveHttpStatusCode404AfterTryingToDelete() throws Exception {
    final Long NOT_EXISTING_ID = 11111L;

    Mockito.doThrow(new HospitalNotFoundException()).when(mockedHospitalService).delete(NOT_EXISTING_ID);

    mvc.perform(
      delete("/hospital/" + NOT_EXISTING_ID).accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isNotFound())
    .andReturn();

    verify(mockedHospitalService, times(1)).delete(NOT_EXISTING_ID);
  }

  @Test
  @DisplayName("Deve receber http status code 201 e um hospital atualizado após utilizar HospitalService.update")
  public void shouldReceiveHttpStatusCode201AndAnUpdatedHospital() throws Exception{
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
    final Long HOSPITAL_TO_BE_UPDATED_ID = 1L;
    Hospital updatedHospital = Hospital.createFromDto(hospitalDto);
    when(
      mockedHospitalService.update(eq(HOSPITAL_TO_BE_UPDATED_ID),isA(HospitalDto.class))
    ).thenReturn(updatedHospital);

    final ObjectMapper objectMapper = new ObjectMapper();
    final String hospitalDtoJson = objectMapper.writeValueAsString(hospitalDto);
    MvcResult mvcResult = mvc.perform(
      put("/hospital/" + HOSPITAL_TO_BE_UPDATED_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(hospitalDtoJson)
        .accept(MediaType.APPLICATION_JSON)
    ) 
    .andExpect(status().isOk())
    .andReturn();    
    
    final String hospitalJson = objectMapper.writeValueAsString(updatedHospital);
    assertEquals(
      hospitalJson,
      mvcResult.getResponse().getContentAsString()
    );
  }

  @Test
  @DisplayName("Deve enviar o DTO correto para HospitalService.udpate")
  public void shouldSendTheCorrectDtoToHospitalServiceUpdate(){
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
    final Long HOSPITAL_TO_BE_UPDATED_ID = 1L;
    Hospital updatedHospital = Hospital.createFromDto(hospitalDto);
    when(
      mockedHospitalService.update(HOSPITAL_TO_BE_UPDATED_ID,hospitalDto)
    ).thenReturn(updatedHospital);

    hospitalController.updateHospital(HOSPITAL_TO_BE_UPDATED_ID, hospitalDto);

    verify(mockedHospitalService, times(1)).update(HOSPITAL_TO_BE_UPDATED_ID, hospitalDto);
  }
}
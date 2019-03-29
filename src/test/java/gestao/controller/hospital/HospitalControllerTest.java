package gestao.controller.hospital;

import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

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

  @MockBean(name="hospitalService")
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
    MvcResult r = mvc.perform(
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
    when(mockedHospitalService.findById(1L)).thenReturn(hospital);

    MvcResult result = mvc.perform(
      get("/hospital/1")
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
    when(mockedHospitalService.findAll()).thenReturn(allHospitals);

    MvcResult result = mvc.perform(
      get("/hospital").accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isOk())
    .andReturn();

    ObjectMapper objectMapper = new ObjectMapper();
    assertEquals(
      objectMapper.writeValueAsString(allHospitals), 
      result.getResponse().getContentAsString()
    );

    final List<Hospital> allHospitalsFromHospitalController = hospitalController.findAll();

    verify(mockedHospitalService, times(2)).findAll();
    assertEquals(
      allHospitalsFromHospitalController,
      allHospitals
    );
  }
}
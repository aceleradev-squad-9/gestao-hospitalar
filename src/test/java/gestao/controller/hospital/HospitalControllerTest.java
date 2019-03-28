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
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;

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
  @DisplayName("Deve receber um status code 400 as mensagens de validação para as informações inválidas enviadas.")
  public void shouldReceiveTheCorrectValidationMessages() throws Exception {
    MvcResult k = mvc.perform(
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
}
package gestao.controller.hospital;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.service.hospital.HospitalService;

@WebMvcTest(HospitalController.class)
public class HospitalControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private HospitalService mockedHospitalService;

  @Test
  @DisplayName("Deve receber código http 201 indicando que um novo hospital foi criado.")
  public void shouldReceive201HttpStatusCode() throws Exception{
    HospitalDto hospitalDto = new HospitalDto(
      "Hospital 1", 
      "Descrição do hospital 1", 
      123, 
      new Address(
        1L,
        "Rua do Hospital 1",
        "Cidade do hospital 1",
        "Estado do hospital 1",
        "12345678",
        "23D",
        34.234,
        12.121
      )  
    );

    when(mockedHospitalService.createHospital(hospitalDto)).thenReturn(
      Hospital.createFromDto(hospitalDto)
    );

    ObjectMapper objectMapper = new ObjectMapper();
    String hospitalDtoJson = objectMapper.writeValueAsString(hospitalDto);
  
    mvc.perform(
      post("/hospital")
        .contentType(MediaType.APPLICATION_JSON)
        .content(hospitalDtoJson)
    )
    .andExpect(status().isCreated());    
  }


}
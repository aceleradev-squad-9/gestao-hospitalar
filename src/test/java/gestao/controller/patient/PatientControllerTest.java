package gestao.controller.patient;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import gestao.service.patient.PatientService;

@WebMvcTest(controllers = PatientController.class)
public class PatientControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private PatientService patientService;

	@InjectMocks
	@Autowired
	private PatientController patientController;
	
}

package gestao.controller.hospital;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.exception.patient.PatientAlreadyHasCheckInOnHospitalException;
import gestao.exception.patient.PatientNotFoundException;
import gestao.exception.person.PersonNotFoundException;
import gestao.helper.hospital.HospitalHelper;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.model.patient.LocalizationDto;
import gestao.model.patient.Patient;
import gestao.model.person.Gender;
import gestao.model.person.Person;
import gestao.service.hospital.HospitalService;
import gestao.service.patient.PatientService;
import gestao.service.person.PersonService;

@WebMvcTest(controllers = HospitalPatientController.class)
public class HospitalPatientControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PatientService patientService;

	@MockBean
	private PersonService personService;

	@MockBean
	private HospitalService hospitalService;

	@InjectMocks
	@Autowired
	private HospitalPatientController hospitalPatientController;

	@Test
	@DisplayName("Deve receber status http 201 e o body em formato json com as informações de um paciente que fez o check-in.")
	public void shouldReceiveStatusCreatedAndBodyWithPatientWhenDoCheckIn() throws Exception {
		Long hospitalId = 1L;
		Long personId = 1L;

		Person person = Person.builder().withName("Pessoa").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();

		Hospital hospital = Hospital
				.createFromDto(new HospitalDto("Hospital A", "Descrição.", 3, Mockito.mock(Address.class)));

		Patient expectedPatient = Patient.builder().withId(1L).withPerson(person).withHospital(hospital)
				.withTimeCheckIn(LocalDateTime.now()).build();

		Mockito.when(personService.findById(personId)).thenReturn(person);
		Mockito.when(hospitalService.findById(hospitalId)).thenReturn(hospital);
		Mockito.when(patientService.checkIn(person, hospital)).thenReturn(expectedPatient);

		ObjectMapper objectMapper = new ObjectMapper();
		String patientJson = objectMapper.writeValueAsString(expectedPatient.convertToDto());

		MvcResult mvcResult = mvc
				.perform(post(String.format("/hospital/%s/checkin/%s", hospitalId, personId))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn();

		assertEquals(patientJson, mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber http status 404 ao tentar realizar um check-in em um hospital não cadastrado.")
	public void shouldReceiveHospitalNotFoundWhenDoCheckIn() throws Exception {
		Long hospitalId = 1L;
		Long personId = 1L;

		Person person = Person.builder().withName("Pessoa").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();

		Mockito.when(personService.findById(personId)).thenReturn(person);
		Mockito.when(hospitalService.findById(hospitalId)).thenThrow(new HospitalNotFoundException());

		MvcResult mvcResult = mvc
				.perform(post(String.format("/hospital/%s/checkin/%s", hospitalId, personId))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
		Mockito.verify(patientService, Mockito.times(0)).checkIn(Mockito.any(), Mockito.any());
	}

	@Test
	@DisplayName("Deve receber http status 404 ao tentar realizar um check-in com uma pessoa não cadastrada.")
	public void shouldReceivePersonNotFoundWhenDoCheckIn() throws Exception {

		Long hospitalId = 1L;
		Long personId = 1L;

		Mockito.when(personService.findById(personId)).thenThrow(new PersonNotFoundException());

		MvcResult mvcResult = mvc
				.perform(post(String.format("/hospital/%s/checkin/%s", hospitalId, personId))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
		Mockito.verify(hospitalService, Mockito.times(0)).findById(Mockito.anyLong());
		Mockito.verify(patientService, Mockito.times(0)).checkIn(Mockito.any(), Mockito.any());
	}

	@Test
	@DisplayName("Deve receber http status 409 ao tentar realizar um check-in para uma pessoa que já possui um check-in em aberto no hospital.")
	public void shouldNotDoesCheckInWhenPatientAlreadyHasDone() throws Exception {

		Long hospitalId = 1L;
		Long personId = 1L;

		Person person = Mockito.mock(Person.class);
		Hospital hospital = Mockito.mock(Hospital.class);

		Mockito.when(personService.findById(personId)).thenReturn(person);
		Mockito.when(hospitalService.findById(hospitalId)).thenReturn(hospital);
		Mockito.when(patientService.checkIn(person, hospital))
				.thenThrow(new PatientAlreadyHasCheckInOnHospitalException());

		MvcResult mvcResult = mvc
				.perform(post(String.format("/hospital/%s/checkin/%s", hospitalId, personId))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber status http 200 e o body em formato json com as informações de um paciente que fez o check-out.")
	public void shouldReceiveStatusOkAndBodyWithPatientWhenDoCheckOut() throws Exception {
		Long patientId = 1L;

		Person person = Person.builder().withName("Pessoa").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();

		Hospital hospital = Hospital
				.createFromDto(new HospitalDto("Hospital A", "Descrição.", 3, Mockito.mock(Address.class)));

		Patient expectedPatient = Patient.builder().withId(1L).withPerson(person).withHospital(hospital)
				.withTimeCheckIn(LocalDateTime.now()).build();

		expectedPatient.doCheckOut();

		Mockito.when(patientService.checkOut(patientId)).thenReturn(expectedPatient);

		ObjectMapper objectMapper = new ObjectMapper();
		String patientJson = objectMapper.writeValueAsString(expectedPatient.convertToDto());

		MvcResult mvcResult = mvc
				.perform(put(String.format("/hospital/patients/%s/checkout", patientId))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertEquals(patientJson, mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber uma lista de todos os pacientes do hospital que não fizeram check-out, em formato json.")
	public void shouldReceiveAListWithAllPatientsFromHospital() throws Exception {
		Long hospitalId = 1L;

		Hospital hospital = Hospital
				.createFromDto(new HospitalDto("Hospital A", "Descrição.", 3, Mockito.mock(Address.class)));

		Patient patientA = Patient.builder().withId(1L)
				.withPerson(Person.builder().withName("Pessoa A").withCpf("65413936052")
						.withDateOfBirth(LocalDate.of(1999, 5, 9)).withGender(Gender.FEMALE).build())
				.withHospital(hospital).withTimeCheckIn(LocalDateTime.now()).build();

		Patient patientB = Patient.builder().withId(2L)
				.withPerson(Person.builder().withName("Pessoa B").withCpf("09532930094")
						.withDateOfBirth(LocalDate.of(1992, 6, 24)).withGender(Gender.OTHERS).build())
				.withHospital(hospital).withTimeCheckIn(LocalDateTime.now()).build();

		List<Patient> patientList = Arrays.asList(patientA, patientB);
		Page<Patient> patientListPage = new PageImpl<>(patientList);

		Mockito.when(hospitalService.findById(hospitalId)).thenReturn(hospital);
		Mockito.when(patientService.findHospitalPatients(Mockito.isA(Hospital.class), Mockito.isA(PageRequest.class)))
				.thenReturn(patientListPage);

		ObjectMapper objectMapper = new ObjectMapper();
		String patientListJson = objectMapper.writeValueAsString(patientListPage.map(Patient::convertToDto));

		MvcResult mvcResult = mvc
				.perform(get(String.format("/hospital/%s/patients?page=0&size=%s", hospitalId, patientList.size()))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertEquals(patientListJson, mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber os dados de um paciente que fez check-in no hospital.")
	public void shouldReceivePatient() throws Exception {
		Long patientId = 1L;

		Person person = Person.builder().withId(1L).withName("Pessoa").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();

		Hospital hospital = Hospital
				.createFromDto(new HospitalDto("Hospital A", "Descrição.", 3, Mockito.mock(Address.class)));

		Patient patient = Patient.builder().withId(patientId).withPerson(person).withHospital(hospital)
				.withTimeCheckIn(LocalDateTime.now()).build();

		Mockito.when(patientService.findById(patientId)).thenReturn(patient);

		ObjectMapper objectMapper = new ObjectMapper();
		String patientJson = objectMapper.writeValueAsString(patient.convertToDto());

		MvcResult mvcResult = mvc.perform(get(String.format("/hospital/patients/%s", patientId))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();

		assertEquals(patientJson, mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve lançar uma exceção ao buscar um paciente que não existe.")
	public void shouldReceivePatientNotFoundWhenDontFindAnPatient() throws Exception {
		Long patientId = 1L;

		Mockito.when(patientService.findById(patientId)).thenThrow(new PatientNotFoundException());

		MvcResult mvcResult = mvc.perform(get(String.format("/hospital/patients/%s", patientId))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve buscar o hospital mais próximo a partir de uma localização.")
	public void shouldReturnNearestHospital() throws Exception {

		Hospital hospitalA = HospitalHelper.getAHospitalWithValidProperties(1L);
		Hospital hospitalB = HospitalHelper.getAHospitalWithValidProperties(2L);

		List<Hospital> hospitalList = Arrays.asList(hospitalA, hospitalB);

		LocalizationDto localizationDto = new LocalizationDto(-8.049290, -34.945709);

		Mockito.when(patientService.countNumberOfBedsOccupied(hospitalA)).thenReturn(0L);
		Mockito.when(patientService.countNumberOfBedsOccupied(hospitalB)).thenReturn(0L);
		Mockito.when(hospitalService.findNearestHospitals(Mockito.any(LocalizationDto.class))).thenReturn(hospitalList);

		ObjectMapper objectMapper = new ObjectMapper();
		String nearestHospitalJson = objectMapper.writeValueAsString(hospitalA);

		MvcResult mvcResult = mvc
				.perform(get("/hospital/nearest").param("latitude", String.valueOf(localizationDto.getLatitude()))
						.param("longitude", String.valueOf(localizationDto.getLongitude()))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertEquals(nearestHospitalJson, mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve buscar o hospital mais próximo, com leitos disponíveis, a partir de uma localização.")
	public void shouldReturnNearestHospitalWithAvailableBeds() throws Exception {

		Hospital hospitalA = HospitalHelper.getAHospitalWithValidProperties(1L);
		Hospital hospitalB = HospitalHelper.getAHospitalWithValidProperties(2L);

		List<Hospital> hospitalList = Arrays.asList(hospitalA, hospitalB);

		LocalizationDto localizationDto = new LocalizationDto(-8.049290, -34.945709);

		Mockito.when(patientService.countNumberOfBedsOccupied(hospitalA))
				.thenReturn(hospitalA.getMaximumNumberOfBeds().longValue());
		Mockito.when(patientService.countNumberOfBedsOccupied(hospitalB)).thenReturn(0L);
		Mockito.when(hospitalService.findNearestHospitals(Mockito.any(LocalizationDto.class))).thenReturn(hospitalList);

		ObjectMapper objectMapper = new ObjectMapper();
		String nearestHospitalJson = objectMapper.writeValueAsString(hospitalB);

		MvcResult mvcResult = mvc
				.perform(get("/hospital/nearest").param("latitude", String.valueOf(localizationDto.getLatitude()))
						.param("longitude", String.valueOf(localizationDto.getLongitude()))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertEquals(nearestHospitalJson, mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve lançar uma exceção caso não existam hospitais próximo, com leitos disponíveis, a partir de uma localização.")
	public void shouldThrowsExceptionWhenNoHospitalAbleToReceivePatients() throws Exception {

		Hospital hospitalA = HospitalHelper.getAHospitalWithValidProperties(1L);
		Hospital hospitalB = HospitalHelper.getAHospitalWithValidProperties(2L);

		List<Hospital> hospitalList = Arrays.asList(hospitalA, hospitalB);

		LocalizationDto localizationDto = new LocalizationDto(-8.049290, -34.945709);

		Mockito.when(patientService.countNumberOfBedsOccupied(hospitalA))
				.thenReturn(hospitalA.getMaximumNumberOfBeds().longValue());
		Mockito.when(patientService.countNumberOfBedsOccupied(hospitalB))
				.thenReturn(hospitalB.getMaximumNumberOfBeds().longValue());
		Mockito.when(hospitalService.findNearestHospitals(Mockito.any(LocalizationDto.class))).thenReturn(hospitalList);

		MvcResult mvcResult = mvc
				.perform(get("/hospital/nearest").param("latitude", String.valueOf(localizationDto.getLatitude()))
						.param("longitude", String.valueOf(localizationDto.getLongitude()))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber errors de validação caso a localização informada seja inválida.")
	public void shouldReceiveValidationErrorsWhenLocalizationIsInvalid() throws Exception {

		Hospital hospitalA = HospitalHelper.getAHospitalWithValidProperties(1L);
		Hospital hospitalB = HospitalHelper.getAHospitalWithValidProperties(2L);

		List<Hospital> hospitalList = Arrays.asList(hospitalA, hospitalB);

		LocalizationDto localizationDto = new LocalizationDto(-91.0, 181.0);

		Mockito.when(patientService.countNumberOfBedsOccupied(hospitalA))
				.thenReturn(hospitalA.getMaximumNumberOfBeds().longValue());
		Mockito.when(patientService.countNumberOfBedsOccupied(hospitalB))
				.thenReturn(hospitalB.getMaximumNumberOfBeds().longValue());
		Mockito.when(hospitalService.findNearestHospitals(Mockito.any(LocalizationDto.class))).thenReturn(hospitalList);

		mvc.perform(get("/hospital/nearest")
				.param("latitude", String.valueOf(localizationDto.getLatitude()))
				.param("longitude", String.valueOf(localizationDto.getLongitude()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.['latitude'][0]",
						is("A latitude deve estar contida no intervalo [-90, 90]")))
				.andExpect(jsonPath("$.errors.['longitude'][0]",
						is("A longitude deve estar contida no intervalo [-180, 180]")))
				.andReturn();
	}
	
	@Test
	@DisplayName("Deve receber errors de validação caso a localização não seja fornecida.")
	public void shouldReceiveValidationErrorsWhenLocalizationIsNotInformed() throws Exception {

		Hospital hospitalA = HospitalHelper.getAHospitalWithValidProperties(1L);
		Hospital hospitalB = HospitalHelper.getAHospitalWithValidProperties(2L);

		List<Hospital> hospitalList = Arrays.asList(hospitalA, hospitalB);

		Mockito.when(patientService.countNumberOfBedsOccupied(hospitalA))
				.thenReturn(hospitalA.getMaximumNumberOfBeds().longValue());
		Mockito.when(patientService.countNumberOfBedsOccupied(hospitalB))
				.thenReturn(hospitalB.getMaximumNumberOfBeds().longValue());
		Mockito.when(hospitalService.findNearestHospitals(Mockito.any(LocalizationDto.class))).thenReturn(hospitalList);

		mvc.perform(get("/hospital/nearest")
				.param("latitude", "")
				.param("longitude", "")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.['latitude'][0]", is("A latitude não deve ser nula e deve ser um número real.")))
				.andExpect(jsonPath("$.errors.['longitude'][0]",is("A longitude não deve ser nula e deve ser um número real.")))
				.andReturn();
	}
}

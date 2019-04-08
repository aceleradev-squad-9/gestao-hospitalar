package gestao.service.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import gestao.exception.hospital.NoHospitalAbleToReceivePatientsException;
import gestao.exception.patient.PatientAlreadyHasCheckInOnHospitalException;
import gestao.exception.patient.PatientNotFoundException;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.model.patient.Patient;
import gestao.model.person.Person;
import gestao.repository.patient.PatientRepository;
import gestao.service.hospital.HospitalService;
import gestao.service.person.PersonService;

@SpringBootTest
public class PatientServiceTest {

	@MockBean
	private HospitalService hospitalService;

	@MockBean
	private PersonService personService;

	@MockBean
	private PatientRepository repository;

	@InjectMocks
	@Autowired
	private PatientService service;

	@Test
	@DisplayName("Deve fazer check-in como um paciente.")
	public void shouldCheckIn() {

		Person person = Mockito.mock(Person.class);
		Hospital hospital = Hospital
				.createFromDto(new HospitalDto("Hospital", "Descrição", 10, Mockito.mock(Address.class)));

		Patient expectedPatient = Patient.builder().withId(1L).withPerson(person).withHospital(hospital)
				.withTimeCheckIn(LocalDateTime.now()).build();

		Mockito.when(repository.countByHospitalAndTimeCheckOutIsNull(hospital)).thenReturn(0L);

		Mockito.when(repository.existsByHospitalAndPersonAndTimeCheckOutIsNull(hospital, person))
				.thenReturn(Boolean.FALSE);

		Mockito.when(repository.save(Mockito.any(Patient.class))).then(returnsFirstArg());

		Patient obtainedPatient = this.service.checkIn(person, hospital);

		assertEquals(expectedPatient.getPerson(), obtainedPatient.getPerson());
		assertEquals(expectedPatient.getHospital(), obtainedPatient.getHospital());
		assertTrue(expectedPatient.getTimeCheckIn().isBefore(obtainedPatient.getTimeCheckIn())
				|| expectedPatient.getTimeCheckIn().equals(obtainedPatient.getTimeCheckIn()));
		assertNull(obtainedPatient.getTimeCheckOut());
	}

	@Test
	@DisplayName("Deve lançar uma exceção caso uma pessoa tente fazer um check-in mas já esteja registrado como um paciente.")
	public void shouldNotCheckInWhenAlreadyHasInHospital() {

		Person person = Mockito.mock(Person.class);
		Hospital hospital = Mockito.mock(Hospital.class);

		Mockito.when(repository.existsByHospitalAndPersonAndTimeCheckOutIsNull(hospital, person))
				.thenReturn(Boolean.TRUE);

		assertThrows(PatientAlreadyHasCheckInOnHospitalException.class, () -> service.checkIn(person, hospital));

		Mockito.verify(repository, Mockito.times(0)).countByHospitalAndTimeCheckOutIsNull(Mockito.any());
		Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
	}

	@Test
	@DisplayName("Deve lançar uma exceção caso uma pessoa tente fazer um check-in mas o hospital não possua leitos disponíveis.")
	public void shouldNotCheckInWhenHospitalDoesNotHaveBeds() {

		Person person = Mockito.mock(Person.class);
		Hospital hospital = Hospital
				.createFromDto(new HospitalDto("Hospital", "Descrição", 10, Mockito.mock(Address.class)));

		Mockito.when(repository.existsByHospitalAndPersonAndTimeCheckOutIsNull(hospital, person))
				.thenReturn(Boolean.FALSE);

		Mockito.when(repository.countByHospitalAndTimeCheckOutIsNull(hospital)).thenReturn(11L);

		assertThrows(NoHospitalAbleToReceivePatientsException.class, () -> service.checkIn(person, hospital));

		Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
	}

	@Test
	@DisplayName("Deve fazer check-out de um paciente.")
	public void shouldCheckOut() {

		Long patientId = 1L;

		Person person = Mockito.mock(Person.class);
		Hospital hospital = Mockito.mock(Hospital.class);

		LocalDateTime timeCheckOut = LocalDateTime.now();

		Patient expectedPatient = Patient.builder().withPerson(person).withHospital(hospital)
				.withTimeCheckIn(LocalDateTime.of(19, 2, 2, 14, 20)).build();

		Mockito.when(repository.findById(patientId)).thenReturn(Optional.of(expectedPatient));
		Mockito.when(repository.save(Mockito.any(Patient.class))).then(returnsFirstArg());

		Patient obtainedPatient = this.service.checkOut(patientId);

		assertEquals(expectedPatient.getPerson(), obtainedPatient.getPerson());
		assertEquals(expectedPatient.getHospital(), obtainedPatient.getHospital());
		assertEquals(expectedPatient.getTimeCheckIn(), obtainedPatient.getTimeCheckIn());
		assertTrue(timeCheckOut.isBefore(obtainedPatient.getTimeCheckOut())
				|| timeCheckOut.equals(obtainedPatient.getTimeCheckOut()));
	}

	@Test
	@DisplayName("Deve lançar exceção caso não encontre os dados do paciente ao tentar fazer o check-out.")
	public void shouldNotCheckOutWhenPatientDoesNotExists() {

		Long patientId = 1L;

		Mockito.when(repository.findById(patientId)).thenReturn(Optional.empty());

		assertThrows(PatientNotFoundException.class, () -> this.service.checkOut(patientId));

		Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
	}

	@Test
	@DisplayName("Deve buscar os dados de todos os pacientes que estão ocupando leitos do hospital.")
	public void shouldFindPatientsFromHospital() {

		Hospital hospital = Mockito.mock(Hospital.class);
		Person personA = Mockito.mock(Person.class);
		Person personB = Mockito.mock(Person.class);

		List<Patient> patientList = Arrays.asList(
				Patient.builder().withId(1L).withPerson(personA).withHospital(hospital)
						.withTimeCheckIn(LocalDateTime.of(19, 2, 2, 14, 20)).build(),
				Patient.builder().withId(2L).withPerson(personB).withHospital(hospital)
						.withTimeCheckIn(LocalDateTime.of(22, 3, 3, 13, 20)).build());

		Page<Patient> patientListPage = new PageImpl<>(patientList);

		Pageable pageable = Pageable.unpaged();

		Mockito.when(repository.findAllByHospitalAndTimeCheckOutIsNull(pageable, hospital)).thenReturn(patientListPage);

		Page<Patient> obtainedPatientsPage = service.findHospitalPatients(hospital, pageable);
		List<Patient> obtainedPatients = obtainedPatientsPage.getContent();

		assertIterableEquals(patientList, obtainedPatients);
		obtainedPatients.stream().forEach((patient) -> {
			assertEquals(hospital, patient.getHospital());
			assertNull(patient.getTimeCheckOut());
		});
	}

	@Test
	@DisplayName("Deve buscar os dados de um paciente ocupando o leito de um hospital.")
	public void shouldFindPatient() {

		Long patientId = 1L;

		Hospital hospital = Mockito.mock(Hospital.class);
		Person person = Mockito.mock(Person.class);

		Patient expectedPatient = Patient.builder().withId(1L).withPerson(person).withHospital(hospital)
				.withTimeCheckIn(LocalDateTime.of(19, 2, 2, 14, 20)).build();

		Mockito.when(repository.findById(patientId)).thenReturn(Optional.of(expectedPatient));

		Patient obtainedPatient = this.service.findById(patientId);

		assertEquals(expectedPatient.getId(), obtainedPatient.getId());
		assertEquals(expectedPatient.getPerson(), obtainedPatient.getPerson());
		assertEquals(expectedPatient.getHospital(), obtainedPatient.getHospital());
		assertEquals(expectedPatient.getTimeCheckIn(), obtainedPatient.getTimeCheckIn());
		assertNull(obtainedPatient.getTimeCheckOut());
	}

	@Test
	@DisplayName("Deve lançar uma exceção ao tentar buscar os dados de um paciente que não está ocupando o leito de um hospital.")
	public void shouldNotFindPatientWhenPatientNotExistsInHospital() {

		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		assertThrows(PatientNotFoundException.class, () -> this.service.findById(1L));
	}
}

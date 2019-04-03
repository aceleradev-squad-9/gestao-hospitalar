package gestao.service.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import gestao.exception.person.PersonNotFoundException;
import gestao.exception.person.PersonsWithSameCpfException;
import gestao.model.person.Gender;
import gestao.model.person.Person;
import gestao.repository.person.PersonRepository;

@SpringBootTest
public class PersonServiceTest {
	@MockBean
	private PersonRepository personRepository;

	@InjectMocks
	@Autowired
	private PersonService service;

	@Test
	@DisplayName("Deve cadastrar uma nova pessoa")
	public void shouldCreatePerson() {
		Person person = Person.builder().withName("Pessoa").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();

		Person expectedPerson = Person.builder().withId(1L).withName("Pessoa").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();

		when(personRepository.existsByCpf(person.getCpf())).thenReturn(Boolean.FALSE);
		when(personRepository.save(person)).thenReturn(expectedPerson);

		Person createdPerson = service.create(person);

		assertEquals(expectedPerson.getId(), createdPerson.getId());
		assertEquals(expectedPerson.getName(), createdPerson.getName());
		assertEquals(expectedPerson.getCpf(), createdPerson.getCpf());
		assertEquals(expectedPerson.getDateOfBirth(), createdPerson.getDateOfBirth());
		assertEquals(expectedPerson.getGender(), createdPerson.getGender());
	}

	@Test
	@DisplayName("Deve lançar uma exceção quando tentar cadastrar uma pessoa que possui um cpf já cadastrado por outra")
	public void shouldNotCreatePersonsWithSameCpf() {
		Person person = Person.builder().withName("Pessoa").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();

		when(personRepository.existsByCpf(person.getCpf())).thenReturn(Boolean.TRUE);

		assertThrows(PersonsWithSameCpfException.class, () -> service.create(person));
		Mockito.verify(personRepository, Mockito.times(0)).save(person);
	}

	@Test
	@DisplayName("Deve listar todos as pessoas cadastradas")
	public void shouldFindPersonList() {

		List<Person> personList = Arrays.asList(
				Person.builder().withId(1L).withName("Pessoa A").withCpf("65413936052")
						.withDateOfBirth(LocalDate.of(1998, 12, 01)).withGender(Gender.FEMALE).build(),
				Person.builder().withId(2L).withName("Pessoa B").withCpf("02154505074")
						.withDateOfBirth(LocalDate.of(1995, 5, 24)).withGender(Gender.MALE).build());

		Page<Person> personListPage = new PageImpl<>(personList);

		Pageable pageable = Pageable.unpaged();

		Mockito.when(personRepository.findAll(pageable)).thenReturn(personListPage);

		Page<Person> obtainedPersonListPage = service.find(pageable);

		assertIterableEquals(personList, obtainedPersonListPage.getContent());
	}

	@Test
	@DisplayName("Deve buscar uma pessoa a partir do seu identificador único")
	public void shouldFindPerson() {

		Long personId = 1L;

		Person person = Person.builder().withId(1L).withName("Pessoa").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();

		when(personRepository.findById(personId)).thenReturn(Optional.of(person));

		Person obtainedPerson = service.findById(personId);

		assertEquals(person.getId(), obtainedPerson.getId());
		assertEquals(person.getName(), obtainedPerson.getName());
		assertEquals(person.getCpf(), obtainedPerson.getCpf());
		assertEquals(person.getDateOfBirth(), obtainedPerson.getDateOfBirth());
		assertEquals(person.getGender(), obtainedPerson.getGender());
	}

	@Test
	@DisplayName("Deve lançar uma exceção caso não encontre uma pessoa utilizando o seu identificador único")
	public void shouldNotFindPersonWhenPersonDoesNotExists() {

		Long personId = 1L;

		when(personRepository.findById(personId)).thenReturn(Optional.empty());

		assertThrows(PersonNotFoundException.class, () -> service.findById(personId));
	}

	@Test
	@DisplayName("Deve atualizar o cadastro de uma pessoa")
	public void shouldUpdatePerson() {
		Long personId = 1L;

		Person personWithInfoToUpdate = Person.builder().withName("Pessoa Atualizada").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();

		Person personToBeUpdated = Person.builder().withId(personId).withName("Pessoa").withCpf("02154505074")
				.withDateOfBirth(LocalDate.of(1992, 3, 2)).withGender(Gender.OTHERS).build();

		when(personRepository.findById(personId)).thenReturn(Optional.of(personToBeUpdated));
		when(personRepository.existsByCpfAndIdNot(personWithInfoToUpdate.getCpf(), personId)).thenReturn(Boolean.FALSE);
		when(personRepository.save(personToBeUpdated)).thenReturn(personToBeUpdated);

		Person updatedPerson = service.update(personId, personWithInfoToUpdate);

		assertEquals(personId, updatedPerson.getId());
		assertEquals(personWithInfoToUpdate.getName(), updatedPerson.getName());
		assertEquals(personWithInfoToUpdate.getCpf(), updatedPerson.getCpf());
		assertEquals(personWithInfoToUpdate.getDateOfBirth(), updatedPerson.getDateOfBirth());
		assertEquals(personWithInfoToUpdate.getGender(), updatedPerson.getGender());
	}

	@Test
	@DisplayName("Deve lançar uma exceção caso tente atualizar o cadastro de uma pessoa que não existe")
	public void shouldNotUpdatePersonWhenPersonDoesNotExists() {
		Long personId = 1L;

		when(personRepository.findById(personId)).thenReturn(Optional.empty());

		assertThrows(PersonNotFoundException.class, () -> service.update(personId, Mockito.any()));

		Mockito.verify(personRepository, Mockito.times(0)).existsByCpfAndIdNot(Mockito.anyString(), Mockito.anyLong());
		Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
	}

	@Test
	@DisplayName("Deve lançar uma exceção quando tentar atualizar os dados de uma pessoa com um cpf já cadastrado por outra")
	public void shouldNotUpdatePersonsWithSameCpf() {
		Long personId = 1L;

		Person personWithInfoToUpdate = Person.builder().withName("Pessoa Atualizada").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();
		
		Person person = Person.builder().withId(personId).withName("Pessoa").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();

		when(personRepository.findById(personId)).thenReturn(Optional.of(person));
		when(personRepository.existsByCpfAndIdNot(personWithInfoToUpdate.getCpf(), personId)).thenReturn(Boolean.TRUE);

		assertThrows(PersonsWithSameCpfException.class, () -> service.update(personId, personWithInfoToUpdate));
		Mockito.verify(personRepository, Mockito.times(0)).save(Mockito.any());
	}

	@Test
	@DisplayName("Deve remover o cadastro de um pessoa")
	public void shouldDeletePerson() {
		Long personId = 1L;

		Person person = Person.builder().withId(personId).withName("Pessoa").withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build();

		when(personRepository.findById(personId)).thenReturn(Optional.of(person));

		service.delete(personId);

		Mockito.verify(personRepository, Mockito.times(1)).delete(person);
	}

	@Test
	@DisplayName("Deve lançar uma exceção caso tente remover o cadastro de uma pessoa que não existe")
	public void shouldNotDeletePersonWhenPersonDoesNotExists() {
		Long personId = 1L;

		when(personRepository.findById(personId)).thenReturn(Optional.empty());

		assertThrows(PersonNotFoundException.class, () -> service.delete(personId));

		Mockito.verify(personRepository, Mockito.times(0)).delete(Mockito.any());
	}
}

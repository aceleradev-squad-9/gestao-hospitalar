package gestao.controller.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import gestao.exception.person.PersonNotFoundException;
import gestao.exception.person.PersonsWithSameCpfException;
import gestao.model.person.Gender;
import gestao.model.person.Person;
import gestao.service.person.PersonService;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PersonService personService;

	@InjectMocks
	@Autowired
	private PersonController personController;
	
	@Test
	@DisplayName("Deve receber o body em formato json com as informações da pessoa cadastrada e também deve receber http status code 201.")
	public void shouldReceiveStatusCreatedAndBodyWithPersonInfo() throws Exception {

		Person person = Person.builder()
				.withName("Pessoa")
				.withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24))
				.withGender(Gender.MALE).build();
		
		Person expectedPerson = Person.builder()
				.withId(1L)
				.withName("Pessoa")
				.withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24))
				.withGender(Gender.MALE).build();

		Mockito.when(personService.create(person)).thenReturn(expectedPerson);

		ObjectMapper objectMapper = new ObjectMapper();
		
		String personJson = objectMapper.writeValueAsString(person);
		String expectedPersonJson = objectMapper.writeValueAsString(expectedPerson);
		
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.post("/person")
						.contentType(MediaType.APPLICATION_JSON)
						.content(personJson)
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isCreated())
						.andReturn();

		assertEquals(expectedPersonJson, mvcResult.getResponse().getContentAsString());
	}
	
	
	@Test
	@DisplayName("Deve receber o http status 409 e lançar a exceção PersonsWithSameCpfException ao cadastrar uma pessoa com cpf já cadastrado.")
	public void shouldThrowExceptionWhenCreatePersonWithExistingCpf() throws Exception {

		Person person = Person.builder()
				.withName("Pessoa")
				.withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24))
				.withGender(Gender.MALE).build();

		Mockito.when(personService.create(person)).thenThrow(new PersonsWithSameCpfException());

		ObjectMapper objectMapper = new ObjectMapper();
		
		String personJson = objectMapper.writeValueAsString(person);

		mvc.perform(MockMvcRequestBuilders.post("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(personJson)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andReturn();

		Mockito.verify(personService, Mockito.times(1)).create(person);
	}
	
	@Test
	@DisplayName("Deve receber o http status 400 e os erros de validação em formato json ao cadastrar uma pessoa sem informar os dados")
	public void shouldReceiveValidationErrorsWhenCreatePersonWithoutData() throws Exception {

		Person person = Person.builder()
				.withName("")
				.withCpf("")
				.withDateOfBirth(null)
				.withGender(null).build();

		ObjectMapper objectMapper = new ObjectMapper();
		
		String personJson = objectMapper.writeValueAsString(person);

		mvc.perform(MockMvcRequestBuilders.post("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(personJson)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.name[0]", Matchers.is("A pessoa deve possuir um nome.")))
				.andExpect(jsonPath("$.errors.cpf[0]", Matchers.is("O cpf não foi informado ou é inválido.")))
				.andExpect(jsonPath("$.errors.dateOfBirth[0]", Matchers.is("A data de nascimento não foi informada ou é inválida.")))
				.andExpect(jsonPath("$.errors.gender[0]", Matchers.is("O sexo da pessoa não foi informado ou é inválido.")))
				.andReturn();

		Mockito.verify(personService, Mockito.times(0)).create(Mockito.any());
	}
	
	@Test
	@DisplayName("Deve receber o http status 400 e os erros de validação em formato json ao cadastrar uma pessoa com dados inválidos")
	public void shouldReceiveValidationErrorsWhenCreatePersonWithInvalidData() throws Exception {

		Person person = Person.builder()
				.withName(null)
				.withCpf("1112s")
				.withDateOfBirth(LocalDate.now().plusDays(1))
				.withGender(null).build();

		ObjectMapper objectMapper = new ObjectMapper();
		
		String personJson = objectMapper.writeValueAsString(person);

		mvc.perform(MockMvcRequestBuilders.post("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(personJson)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.name[0]", Matchers.is("A pessoa deve possuir um nome.")))
				.andExpect(jsonPath("$.errors.cpf[0]", Matchers.is("O cpf não foi informado ou é inválido.")))
				.andExpect(jsonPath("$.errors.dateOfBirth[0]", Matchers.is("A data de nascimento deve estar no passado.")))
				.andExpect(jsonPath("$.errors.gender[0]", Matchers.is("O sexo da pessoa não foi informado ou é inválido.")))
				.andReturn();
		
		Mockito.verify(personService, Mockito.times(0)).create(Mockito.any());
	}
	
	@Test
	@DisplayName("Deve receber o http status 200 e uma lista de pessoas cadastradas em formato json")
	public void shouldReceivePersonList() throws Exception {

		List<Person> personList = Arrays.asList(
				Person.builder().withName("Pessoa A").withCpf("654.139.360-52")
						.withDateOfBirth(LocalDate.of(1995, 8, 24)).withGender(Gender.MALE).build(),
				Person.builder().withName("Pessoa B").withCpf("519.159.520-34")
						.withDateOfBirth(LocalDate.of(1998, 10, 10)).withGender(Gender.FEMALE).build());

		Page<Person> personListPage = new PageImpl<>(personList);
		
		Mockito.when(personService.find(isA(PageRequest.class)))
			.thenReturn(personListPage);

		ObjectMapper objectMapper = new ObjectMapper();
		String personListJson = objectMapper.writeValueAsString(personListPage);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
			.get(String.format("/person?page=0&size=%s",personList.size()))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(personListJson, mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber o http status 200 e os dados de uma pessoa em formato json, obtido a partir do seu identificador único.")
	public void shouldReceivePerson() throws Exception {

		Long personId = 1L;

		Person person = Person.builder()
				.withId(personId)
				.withName("Pessoa")
				.withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24))
				.withGender(Gender.MALE).build();

		Mockito.when(personService.findById(personId)).thenReturn(person);

		ObjectMapper objectMapper = new ObjectMapper();
		String personJson = objectMapper.writeValueAsString(person);

		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.get(String.format("/person/%s", personId))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk()).andReturn();

		assertEquals(personJson, mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	@DisplayName("Deve receber o http status 404 e lançar a exceção PersonNotFoundException ao buscar uma pessoa não cadastrada.")
	public void shouldThrowExceptionWhenDontFindPerson() throws Exception {

		Long personId = 1L;

		Mockito.when(personService.findById(personId)).thenThrow(new PersonNotFoundException());

		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders
						.get(String.format("/person/%s", personId))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	@DisplayName("Deve receber o body em formato json com os dados cadastrais de uma pessoa atualizados e também deve receber http status code 200.")
	public void shouldReceiveUpdatedPerson() throws Exception {

		Long personId = 1L;
		
		Person person = Person.builder()
				.withName("Pessoa")
				.withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24))
				.withGender(Gender.MALE).build();
		
		Person updatedPerson = Person.builder()
				.withId(1L)
				.withName("Pessoa")
				.withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24))
				.withGender(Gender.MALE).build();

		Mockito.when(personService.update(personId, person)).thenReturn(updatedPerson);

		ObjectMapper objectMapper = new ObjectMapper();
		
		String personJson = objectMapper.writeValueAsString(person);
		String updatedPersonJson = objectMapper.writeValueAsString(updatedPerson);
		
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(String.format("/person/%s", personId))
						.contentType(MediaType.APPLICATION_JSON)
						.content(personJson)
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andReturn();

		assertEquals(updatedPersonJson, mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	@DisplayName("Deve receber o http status 409 e lançar a exceção PersonsWithSameCpfException ao atualizar o cpf de uma pessoa para um já cadastrado.")
	public void shouldThrowExceptionWhenUpdatePersonToExistingCpf() throws Exception {

		Long personId = 1L;
		
		Person person = Person.builder()
				.withName("Pessoa")
				.withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24))
				.withGender(Gender.MALE).build();

		Mockito.when(personService.update(personId, person)).thenThrow(new PersonsWithSameCpfException());

		ObjectMapper objectMapper = new ObjectMapper();
		
		String personJson = objectMapper.writeValueAsString(person);

		mvc.perform(MockMvcRequestBuilders.put(String.format("/person/%s", personId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(personJson)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andReturn();

		Mockito.verify(personService, Mockito.times(1)).update(personId, person);
	}
	
	@Test
	@DisplayName("Deve receber o http status 400 e os erros de validação em formato json ao atualizar o cadastro de uma pessoa sem informar os dados.")
	public void shouldReceiveValidationErrorsWhenUpdatePersonWithoutData() throws Exception {

		Long personId = 1L;
		
		Person person = Person.builder()
				.withName("")
				.withCpf("")
				.withDateOfBirth(null)
				.withGender(null).build();

		ObjectMapper objectMapper = new ObjectMapper();
		
		String personJson = objectMapper.writeValueAsString(person);
		
		mvc.perform(MockMvcRequestBuilders.put(String.format("/person/%s", personId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(personJson)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.name[0]", Matchers.is("A pessoa deve possuir um nome.")))
				.andExpect(jsonPath("$.errors.cpf[0]", Matchers.is("O cpf não foi informado ou é inválido.")))
				.andExpect(jsonPath("$.errors.dateOfBirth[0]", Matchers.is("A data de nascimento não foi informada ou é inválida.")))
				.andExpect(jsonPath("$.errors.gender[0]", Matchers.is("O sexo da pessoa não foi informado ou é inválido.")))
				.andReturn();

		Mockito.verify(personService, Mockito.times(0)).update(Mockito.anyLong(), Mockito.any());
	}
	
	@Test
	@DisplayName("Deve receber o http status 400 e os erros de validação em formato json ao atualizar o cadastro de uma pessoa com dados inválidos")
	public void shouldReceiveValidationErrorsWhenUpdatePersonWithInvalidData() throws Exception {

		Long personId = 1L;
		
		Person person = Person.builder()
				.withName(null)
				.withCpf("1112s")
				.withDateOfBirth(LocalDate.now().plusDays(1))
				.withGender(null).build();

		ObjectMapper objectMapper = new ObjectMapper();
		
		String personJson = objectMapper.writeValueAsString(person);

		mvc.perform(MockMvcRequestBuilders.put(String.format("/person/%s", personId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(personJson)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.name[0]", Matchers.is("A pessoa deve possuir um nome.")))
				.andExpect(jsonPath("$.errors.cpf[0]", Matchers.is("O cpf não foi informado ou é inválido.")))
				.andExpect(jsonPath("$.errors.dateOfBirth[0]", Matchers.is("A data de nascimento deve estar no passado.")))
				.andExpect(jsonPath("$.errors.gender[0]", Matchers.is("O sexo da pessoa não foi informado ou é inválido.")))
				.andReturn();
		
		Mockito.verify(personService, Mockito.times(0)).update(Mockito.anyLong(), Mockito.any());
	}
	
	@Test
	@DisplayName("Deve receber o http status 404 e lançar a exceção PersonNotFoundException ao tentar atualizar os dados de uma pessoa não cadastrada.")
	public void shouldThrowExceptionWhenDontFindPersonOnUpdate() throws Exception {

		Long personId = 1L;
		
		Person person = Person.builder()
				.withName("Pessoa")
				.withCpf("65413936052")
				.withDateOfBirth(LocalDate.of(1995, 8, 24))
				.withGender(Gender.MALE).build();
		
		Mockito.when(personService.update(personId, person)).thenThrow(new PersonNotFoundException());

		ObjectMapper objectMapper = new ObjectMapper();
		
		String personJson = objectMapper.writeValueAsString(person);
		
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders
						.put(String.format("/person/%s", personId))
						.content(personJson)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	@DisplayName("Deve receber o http status 204 informando que o cadastro da pessoa foi removido com sucesso")
	public void shouldDeletePerson() throws Exception {

		Long personId = 1L;

		mvc.perform(MockMvcRequestBuilders.delete(String.format("/person/%s", personId))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andReturn();
		
		Mockito.verify(personService, Mockito.times(1)).delete(personId);
	}
	
	@Test
	@DisplayName("Deve receber o http status 404 e lançar a exceção PersonNotFoundException ao tentar remover os dados de uma pessoa não cadastrada.")
	public void shouldThrowExceptionWhenDontFindPersonOnDelete() throws Exception {

		Long personId = 1L;
		
		Mockito.doThrow(new PersonNotFoundException()).when(personService).delete(personId);

		mvc.perform(MockMvcRequestBuilders.delete(String.format("/person/%s", personId))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
		
		Mockito.verify(personService, Mockito.times(1)).delete(personId);
	}
	
	
}

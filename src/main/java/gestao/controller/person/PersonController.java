package gestao.controller.person;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gestao.model.person.Person;
import gestao.service.person.PersonService;

/**
 * Controlador dos serviços relacionados a pessoa.
 * 
 * @author edmilson.santana
 *
 */
@RestController
@RequestMapping("person")
public class PersonController {

	@Autowired
	private PersonService service;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Person create(@RequestBody @Valid Person person) {
		return this.service.create(person);
	}

	@GetMapping
	public Iterable<Person> find() {
		return this.service.find();
	}

	@GetMapping("/{id}")
	public Person find(@PathVariable Long id) {
		return this.service.find(id);
	}

	@PutMapping
	public Person update(@RequestBody @Valid Person person) {
		return this.service.update(person);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		this.service.delete(id);
	}
}

package gestao.controller.person;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@Autowired
	private PersonResourceMapper resourceMapper;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PersonResource create(@RequestBody @Valid Person person) {
		return resourceMapper.toResource(this.service.create(person));
	}

	@GetMapping(params = { "page", "size" })
	public PagedResources<PersonResource> find(@RequestParam int page, @RequestParam int size,
			PagedResourcesAssembler<Person> pagedResourcesAssembler) {

		Page<Person> personPage = this.service.find(PageRequest.of(page, size));

		return pagedResourcesAssembler.toResource(personPage, resourceMapper);
	}

	@GetMapping("/{id}")
	public PersonResource find(@PathVariable Long id) {

		return resourceMapper.toResource(this.service.findById(id));
	}

	@PutMapping("/{id}")
	public PersonResource update(@PathVariable Long id, @RequestBody @Valid Person person) {
		return resourceMapper.toResource(this.service.update(id, person));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		this.service.delete(id);
	}
}

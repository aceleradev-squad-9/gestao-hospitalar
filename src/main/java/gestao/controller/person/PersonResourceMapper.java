package gestao.controller.person;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import gestao.model.person.Person;

@Component
public class PersonResourceMapper extends ResourceAssemblerSupport<Person, PersonResource> {

	private static final String UPDATE = "update";
	private static final String DELETE = "delete";
	private static final Class<PersonController> controllerClass = PersonController.class;

	public PersonResourceMapper() {
		super(controllerClass, PersonResource.class);
	}

	@Override
	public PersonResource toResource(Person person) {

		final Long id = person.getId();

		PersonResource resource = this.createResourceWithId(id, person);

		resource.add(linkTo(controllerClass).slash(id).withRel(DELETE));
		resource.add(linkTo(controllerClass).slash(id).withRel(UPDATE));

		return resource;
	}

	@Override
	protected PersonResource instantiateResource(Person entity) {
		return new PersonResource(
				entity.getId(), 
				entity.getName(), 
				entity.getCpf(), 
				entity.getDateOfBirth(),
				entity.getGender());
	}

}

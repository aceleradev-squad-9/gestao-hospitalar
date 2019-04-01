package gestao.service.person;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gestao.exception.person.PersonNotFoundException;
import gestao.exception.person.PersonsWithSameCpfException;
import gestao.model.person.Person;
import gestao.repository.person.PersonRepository;

/**
 * Classe responsável pela implementação dos serviços relacionados a pessoa.
 * 
 * @author edmilson.santana
 *
 */
@Service
public class PersonService {

	@Autowired
	private PersonRepository repository;

	/**
	 * Método responsável por criar uma uma pessoa. Caso o cpf da pessoa já esteja
	 * cadastrado, é lançada a exceção {@link PersonsWithSameCpfException}.
	 * 
	 * @param person - {@link Person}
	 * @return pessoa criada - {@link Person}
	 */
	public Person create(Person person) {

		if (this.existsByCpf(person.getCpf())) {
			throw new PersonsWithSameCpfException();
		}

		return this.repository.save(person);
	}

	/**
	 * Método responsável por obter uma lista de pessoas.
	 * 
	 * @return lista de pessoas - {@link List}
	 */
	public Page<Person> find(Pageable page) {		
		return this.repository.findAll(page);
	}

	/**
	 * Método responsável por obter uma pessoa a partir do seu identificador único.
	 * Caso a pessoa não seja encontrada, o método lança um
	 * {@link PersonNotFoundException}.
	 * 
	 * @param id - {@link Long}
	 * @return pessoa - {@link Person}
	 */
	public Person find(Long id) {
		return this.repository.findById(id).orElseThrow(() -> new PersonNotFoundException());
	}

	/**
	 * Método responsável por verificar se uma pessoa existe utilizando o seu
	 * identificador único.
	 * 
	 * @param id - {@link Long}
	 * @return retorna true, caso a pessoa exista e false, caso não exista. -
	 *         {@link Boolean}
	 */
	private Boolean existsById(Long id) {
		return this.repository.existsById(id);
	}

	/**
	 * Método responsável por verificar se uma pessoa já cadastrada possui um cpf
	 * igual a uma outra.
	 * 
	 * @param cpf - {@link String}
	 * @param id  - {@link Long}
	 * @return retorna true, caso existam pessoas com o mesmo cpf e false, caso não
	 *         exista. - {@link Boolean}
	 */
	private Boolean existsPersonWithSameCPF(String cpf, Long id) {
		return this.repository.existsByCpfAndIdNot(cpf, id);
	}

	/**
	 * Método responsável por verificar se uma pessoa está cadastrada, utilizando o
	 * seu cpf.
	 * 
	 * @param cpf - {@link String}
	 * @return retorna true, caso a pessoa exista e false, caso não exista. -
	 *         {@link Boolean}
	 */
	private Boolean existsByCpf(String cpf) {
		return this.repository.existsByCpf(cpf);
	}

	/**
	 * Método responsável por atualizar uma pessoa. Caso a pessoa não seja
	 * encontrada, o método lança um {@link PersonNotFoundException}.
	 * 
	 * @param person - {@link Person}
	 * @return pessoa atualizada - {@link Person}
	 */
	public Person update(Person person) {

		if (!this.existsById(person.getId())) {
			throw new PersonNotFoundException();
		}

		if (this.existsPersonWithSameCPF(person.getCpf(), person.getId())) {
			throw new PersonsWithSameCpfException();
		}

		return this.repository.save(person);
	}

	/**
	 * Método responsável por remover uma pessoa a partir do seu identificador
	 * único. Caso a pessoa não seja encontrada, o método lança um
	 * {@link PersonNotFoundException}.
	 * 
	 * @param id - {@link Long}
	 */
	public void delete(Long id) {
		this.repository.delete(this.find(id));
	}

}

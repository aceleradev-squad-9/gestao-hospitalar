package gestao.repository.person;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gestao.model.person.Person;

/**
 * Interface responsável pelo repositório de dados de uma pessoa.
 * 
 * @author edmilson.santana
 *
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

	Boolean existsByCpf(String cpf);
	
	Boolean existsByCpfAndIdNot(String cpf, Long id);

}

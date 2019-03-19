package gestao.repository.pessoa;

import org.springframework.data.mongodb.repository.MongoRepository;

import gestao.model.pessoa.Pessoa;

/**
 * Interface responsável pelo repositório de dados de {@link Pessoa}
 * 
 * @author edmilson.santana
 *
 */
public interface PessoaRepository extends MongoRepository<Pessoa, String> {
	

}

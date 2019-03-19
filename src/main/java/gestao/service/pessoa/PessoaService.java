package gestao.service.pessoa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestao.exception.pessoa.PessoaNaoEncontradaException;
import gestao.model.pessoa.Pessoa;
import gestao.repository.pessoa.PessoaRepository;

/**
 * Classe responsável pela implementação dos serviços de uma {@link Pessoa}
 * 
 * @author edmilson.santana
 *
 */
@Service
public class PessoaService {

	@Autowired
	private PessoaRepository repository;

	/**
	 * Método responsável por criar uma {@link Pessoa}.
	 * 
	 * @param pessoa - {@link Pessoa}
	 * @return pessoa criada - {@link Pessoa}
	 */
	public Pessoa create(Pessoa pessoa) {
		return this.repository.save(pessoa);
	}

	/**
	 * Método responsável por obter uma lista de pessoas.
	 * 
	 * @return lista de pessoas - {@link List}
	 */
	public List<Pessoa> find() {
		return this.repository.findAll();
	}

	/**
	 * Método responsável por obter uma pessoa a partir do seu identificador único.
	 * Caso a pessoa não seja encontrada, o método lança um
	 * {@link PessoaNaoEncontradaException}.
	 * 
	 * @param id - {@link String}
	 * @return pessoa - {@link Pessoa}
	 */
	public Pessoa find(String id) {
		return this.repository.findById(id).orElseThrow(() -> new PessoaNaoEncontradaException());
	}

	/**
	 * Método responsável por verificar se uma pessoa existe utilizando o seu
	 * identificador único. Caso a pessoa não seja encontrada, o método lança um
	 * {@link PessoaNaoEncontradaException}.
	 * 
	 * @param id - {@link String}
	 */
	private void exists(String id) {
		if (!this.repository.existsById(id)) {
			throw new PessoaNaoEncontradaException();
		}
	}

	/**
	 * Método responsável por atualizar uma {@link Pessoa}.Caso a pessoa não seja
	 * encontrada, o método lança um {@link PessoaNaoEncontradaException}.
	 * 
	 * @param id - {@link String}
	 * @return pessoa - {@link Pessoa}
	 */
	public Pessoa update(Pessoa pessoa) {

		this.exists(pessoa.getId());

		return this.repository.save(pessoa);
	}

	/**
	 * Método responsável por remover uma {@link Pessoa} a partir do seu
	 * identificador único. Caso a pessoa não seja encontrada, o método lança um
	 * {@link PessoaNaoEncontradaException}.
	 * 
	 * @param id - {@link String}
	 */
	public void delete(String id) {
		this.repository.delete(this.find(id));
	}

}

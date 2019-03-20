package gestao.service.pessoa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestao.exception.pessoa.PessoasComCpfIgualException;
import gestao.exception.pessoa.PessoaNaoEncontradaException;
import gestao.model.pessoa.Pessoa;
import gestao.repository.pessoa.PessoaRepository;

/**
 * Classe responsável pela implementação dos serviços de {@link Pessoa}.
 * 
 * @author edmilson.santana
 *
 */
@Service
public class PessoaService {

	@Autowired
	private PessoaRepository repository;

	/**
	 * Método responsável por criar uma {@link Pessoa}. Caso o cpf da pessoa já
	 * esteja cadastrado, é lançada a exceção
	 * {@link PessoasComCpfIgualException}.
	 * 
	 * @param pessoa - {@link Pessoa}
	 * @return pessoa criada - {@link Pessoa}
	 */
	public Pessoa create(Pessoa pessoa) {

		if (this.existsByCpf(pessoa.getCpf())) {
			throw new PessoasComCpfIgualException();
		}

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
	 * identificador único.
	 * 
	 * @param id - {@link String}
	 * @return retorna true, caso a pessoa exista e false, caso não exista. -
	 *         {@link Boolean}
	 */
	private Boolean existsById(String id) {
		return this.repository.existsById(id);
	}

	/**
	 * Método responsável por verificar se uma pessoa já cadastrada possui um cpf
	 * igual a uma outra.
	 * 
	 * @param cpf - {@link String}
	 * @param id  - {@link String}
	 * @return retorna true, caso existam pessoas com o mesmo cpf e false, caso não
	 *         exista. - {@link Boolean}
	 */
	private Boolean existsPersonWithSameCPF(String cpf, String id) {
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
	 * Método responsável por atualizar uma {@link Pessoa}. Caso a pessoa não seja
	 * encontrada, o método lança um {@link PessoaNaoEncontradaException}.
	 * 
	 * @param id - {@link String}
	 * @return pessoa - {@link Pessoa}
	 */
	public Pessoa update(Pessoa pessoa) {

		if (!this.existsById(pessoa.getId())) {
			throw new PessoaNaoEncontradaException();
		}

		if (this.existsPersonWithSameCPF(pessoa.getCpf(), pessoa.getId())) {
			throw new PessoasComCpfIgualException();
		}

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

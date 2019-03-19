package gestao.controller.pessoa;

import java.util.List;

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

import gestao.model.pessoa.Pessoa;
import gestao.service.pessoa.PessoaService;

@RestController
@RequestMapping("pessoa")
public class PessoaController {

	@Autowired
	private PessoaService service;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Pessoa create(@RequestBody Pessoa pessoa) {
		return this.service.create(pessoa);
	}

	@GetMapping
	public List<Pessoa> find() {
		return this.service.find();
	}

	@GetMapping("/{id}")
	public Pessoa find(@PathVariable String id) {
		return this.service.find(id);
	}

	@PutMapping
	public Pessoa update(@RequestBody Pessoa pessoa) {
		return this.service.update(pessoa);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String id) {
		this.service.delete(id);
	}
}

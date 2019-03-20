package gestao.model.pessoa;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Classe responsável pela representação de uma Pessoa.
 * 
 * @author edmilson.santana
 *
 */
@Document(collection = "pessoa")
public class Pessoa {

	@Id
	private String id;
	
	@NotBlank(message="O nome é obrigatório.")
	private String nome;
	
	@CPF(message="O cpf informado é inválido.")
	private String cpf;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dataNascimento;
	
	@NotNull(message="O sexo da pessoa é obrigatório.")
	private Sexo sexo;
	
	Pessoa() {
		
	}
	
	Pessoa(String nome, String cpf, LocalDate dataNascimento, Sexo sexo) {
		this.nome = nome;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.sexo = sexo;
	}

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getCpf() {
		return cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public Sexo getSexo() {
		return sexo;
	}

}

package gestao.model.pessoa;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gestao.model.converter.DateDeserializer;
import gestao.model.converter.DateSerializer;

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

	@NotBlank(message = "O nome é obrigatório.")
	private String nome;

	@CPF(message = "O cpf não foi informado ou é inválido.")
	private String cpf;

	@NotNull(message = "A data de nascimento não foi informada ou é inválida.")
	@Past(message = "A data de nascimento deve estar no passado.")
	@JsonDeserialize(using = DateDeserializer.class)
	@JsonSerialize(using = DateSerializer.class)
	private LocalDate dataNascimento;

	@NotNull(message = "O sexo da pessoa não foi informado ou é inválido.")
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

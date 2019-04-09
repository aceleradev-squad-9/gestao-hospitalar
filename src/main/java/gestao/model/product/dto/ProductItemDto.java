package gestao.model.product.dto;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gestao.util.jackson.DateDeserializer;
import gestao.util.jackson.DateSerializer;

public class ProductItemDto {

	private Long id;

	private String name;

	private String description;

	@NotNull(message = "Informe a quantidade do produto")
	@Min(value = 1, message = "A quantidade do produto deve ser maior que 0")
	private Integer amount;

	@NotNull(message = "A data de validade não foi informada ou é inválida.")
	@Future(message = "A data de validade deve estar no futuro.")
	@JsonDeserialize(using = DateDeserializer.class)
	@JsonSerialize(using = DateSerializer.class)
	private LocalDate expirationDate;

	public Integer getAmount() {
		return amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

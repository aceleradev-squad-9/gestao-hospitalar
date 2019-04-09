package gestao.model.product.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductAmountDto {

	@NotNull(message = "Informe a quantidade do produto")
	@Min(value = 1, message = "A quantidade do produto deve ser maior que 0")
	private Integer amount;

	@JsonCreator
	public ProductAmountDto(@JsonProperty("amount") Integer amount) {
		this.amount = amount;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}

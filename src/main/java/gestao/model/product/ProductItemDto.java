package gestao.model.product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProductItemDto {

	private String name;

	private String description;

	@NotNull(message="Informe a quantidade do produto.")
	@Min(value = 1)
	private Integer amount;

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

}

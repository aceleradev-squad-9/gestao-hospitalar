package gestao.model.product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProductItemDto {

	private String name;

	private String description;

	@NotNull
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

	public static ProductItemDto convertToDto(ProductItem productItem) {
		ProductItemDto productItemDto = null;
		if (productItem != null) {
			productItemDto = new ProductItemDto();
			productItemDto.setAmount(productItem.getAmount());
			productItemDto.setName(productItem.getProductName());
			productItemDto.setDescription(productItem.getProductDescription());
		}
		return productItemDto;
	}

}

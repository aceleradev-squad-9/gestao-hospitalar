package gestao.model.product;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import gestao.model.hospital.Hospital;

@Entity
public class ProductItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Hospital hospital;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	private Integer amount;

	ProductItem() {

	}

	public ProductItem(Product product, Integer amount) {
		this.product = product;
		this.amount = amount;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAmount() {
		return amount;
	}
	
	public String getProductName() {
		return this.product.getName();
	}
	
	public String getProductDescription() {
		return this.product.getDescription();
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

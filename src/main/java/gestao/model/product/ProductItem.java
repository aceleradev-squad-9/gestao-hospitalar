package gestao.model.product;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import gestao.model.BaseEntity;
import gestao.model.hospital.Hospital;


@Entity
@SQLDelete(sql = "UPDATE ProductItem SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class ProductItem extends BaseEntity {

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

	ProductItem(Hospital hospital, Product product, Integer amount) {
		this.hospital = hospital;
		this.product = product;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public Integer getAmount() {
		return amount;
	}

	public Product getProduct() {
		return product;
	}

	public Boolean ableToReduce(Integer amount, Integer minAmount){
		return amount != null && ((this.amount - amount) > minAmount);
	}

	public Boolean reduceAmount(Integer amount, Integer minAmount) {

		Boolean hasReduced = Boolean.FALSE;
		if (this.ableToReduce(amount, minAmount)) {
			this.amount -= amount;
			hasReduced = Boolean.TRUE;
		}
		return hasReduced;
	}

	public void increaseAmount(Integer amount) {
		if (amount != null) {
			this.amount += amount;
		}
	}

	public String getProductName() {
		return this.product.getName();
	}

	public String getProductDescription() {
		return this.product.getDescription();
	}

	public static class ProductItemBuilder {

		private Integer amount;

		private Hospital hospital;

		private Product product;

		public ProductItemBuilder withAmount(Integer amount) {
			this.amount = amount;
			return this;
		}

		public ProductItemBuilder withProduct(Product product) {
			this.product = product;
			return this;
		}

		public ProductItemBuilder withHospital(Hospital hospital) {
			this.hospital = hospital;
			return this;
		}

		public ProductItem build() {
			return new ProductItem(hospital, product, amount);
		}
	}

	public ProductItemDto convertToDto() {

		ProductItemDto productItemDto = new ProductItemDto();
		productItemDto.setAmount(this.getAmount());
		productItemDto.setName(this.getProductName());
		productItemDto.setDescription(this.getProductDescription());

		return productItemDto;
	}

	public static ProductItemBuilder builder() {
		return new ProductItemBuilder();
	}

}

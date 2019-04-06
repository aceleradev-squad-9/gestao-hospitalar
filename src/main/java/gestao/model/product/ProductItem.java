package gestao.model.product;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import gestao.model.BaseEntity;
import gestao.model.hospital.Hospital;


@Entity
@SQLDelete(sql = "UPDATE ProductItem SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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

	public Hospital getHospital(){
		return hospital;
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

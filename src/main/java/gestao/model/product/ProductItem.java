package gestao.model.product;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import gestao.exception.hospital.InvalidAmountInStockException;
import gestao.model.BaseEntity;
import gestao.model.hospital.Hospital;
import gestao.model.product.dto.ProductItemDto;

@Entity
@SQLDelete(sql = "UPDATE product_item SET deleted = true WHERE id = ?")
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
	
	private LocalDate expirationDate;

	ProductItem() {

	}

	ProductItem(Long id, Hospital hospital, Product product, Integer amount, LocalDate expirationDate) {
		this.id = id;
		this.hospital = hospital;
		this.product = product;
		this.amount = amount;
		this.expirationDate = expirationDate;
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

	public Hospital getHospital() {
		return hospital;
	}

	public void increaseAmount(Integer amount) {
		if (amount != null) {
			this.amount += amount;
		}
	}
	
	public void reduceAmount(Integer amount) {
		
		if (amount != null) {
			
			if(this.amount - amount < 0) {
				throw new InvalidAmountInStockException();
			}
			
			this.amount -= amount;
		}
	}

	public String getProductName() {
		return this.product.getName();
	}

	public String getProductDescription() {
		return this.product.getDescription();
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public static class ProductItemBuilder {

		private Long id;
		
		private Integer amount;

		private Hospital hospital;

		private Product product;

		private LocalDate expirationDate;

		public ProductItemBuilder withId(Long id) {
			this.id = id;
			return this;
		}
		
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

		public ProductItemBuilder withExpirationDate(LocalDate expirationDate) {
			this.expirationDate = expirationDate;
			return this;
		}

		public ProductItem build() {
			return new ProductItem(id, hospital, product, amount, expirationDate);
		}
	}

	public ProductItemDto convertToDto() {

		ProductItemDto productItemDto = new ProductItemDto();
		productItemDto.setId(this.getId());
		productItemDto.setAmount(this.getAmount());
		productItemDto.setName(this.getProductName());
		productItemDto.setDescription(this.getProductDescription());
		productItemDto.setExpirationDate(this.getExpirationDate());

		return productItemDto;
	}

	public static ProductItemBuilder builder() {
		return new ProductItemBuilder();
	}

}

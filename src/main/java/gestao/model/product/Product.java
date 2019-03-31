package gestao.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import gestao.model.BaseEntity;

@Entity
@SQLDelete(sql = "UPDATE Product SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@NotBlank(message="O produto deve possuir um nome.")
	private String name;

	@Column
	@NotBlank(message="O produto deve possuir uma descrição.")
	private String description;

	Product() {
		
	}
	
	Product(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public static ProductBuilder builder() {
		return new ProductBuilder();
	}

	public static class ProductBuilder {
		private Long id;

		private String name;

		private String description;

		public ProductBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public ProductBuilder withDescription(String description) {
			this.description = description;
			return this;
		}

		public ProductBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public Product build() {
			return new Product(id, name, description);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

package gestao.model.hospital;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gestao.exception.hospital.ProductNotFoundInHospitalStockException;
import gestao.model.address.Address;
import gestao.model.bloodbank.BloodBankItem;
import gestao.model.patient.Patient;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;

@Entity
public class Hospital {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	private Integer maximumNumberOfBeds;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hospital")
	private List<Patient> patients = new ArrayList<>();

	@OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	private Address address;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hospital")
	private List<ProductItem> stock = new ArrayList<>();

	@OneToMany(mappedBy = "hospital")
	private List<BloodBankItem> bloodBank = new ArrayList<>();

	public static final Integer MIN_STOCK_AMOUNT = 4;

	Hospital() {
	}

	public List<ProductItem> getStock() {
		return Collections.unmodifiableList(this.stock);
	}

	public ProductItem addProductInStock(Product product, Integer amount) {
		ProductItem productItem = this.getProductItem(product).orElse(null);

		if (productItem != null) {
			productItem.increaseAmount(amount);
		} else {
			productItem = ProductItem.builder().withAmount(amount).withProduct(product).withHospital(this).build();
			this.stock.add(productItem);
		}

		return productItem;
	}

	private Optional<ProductItem> getProductItem(Product product) {

		return this.stock.stream().filter((item) -> item.getProduct().equals(product)).findFirst();
	}

	public ProductItem findProductInStock(Product product) {
		return this.getProductItem(product).orElseThrow(() -> new ProductNotFoundInHospitalStockException());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Integer getMaximumNumberOfBeds() {
		return maximumNumberOfBeds;
	}

	public Address getAddress() {
		return address;
	}

	public void updateFromDto(HospitalDto dto) {
		this.name = dto.getName();
		this.description = dto.getDescription();
		this.maximumNumberOfBeds = dto.getMaximumNumberOfBeds();
		this.address = dto.getAddress();
	}

	public static Hospital createFromDto(HospitalDto dto) {
		Hospital hospital = new Hospital();
		hospital.name = dto.getName();
		hospital.description = dto.getDescription();
		hospital.maximumNumberOfBeds = dto.getMaximumNumberOfBeds();
		hospital.address = dto.getAddress();
		return hospital;
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
		Hospital other = (Hospital) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

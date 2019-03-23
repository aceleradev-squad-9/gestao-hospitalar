package gestao.model.hospital;

import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import gestao.model.address.Address;
import gestao.model.patient.Patient;
import gestao.model.product.ProductItem;

@Entity
public class Hospital {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	// capacidade maxima de leitos do hospital
	private Integer beds;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hospital")
	private List<Patient> patients;

	@OneToOne(fetch = FetchType.EAGER)
	private Address address;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hospital")
	private List<ProductItem> stock;

	Hospital() {
	}

	public Hospital(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public void addProductInStock(ProductItem productItem) {
		if (productItem != null) {
			this.stock.add(productItem);
			productItem.setHospital(this);
		}
	}
	
	public List<ProductItem> getStock() {
		return Collections.unmodifiableList(this.stock);
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

	public static Hospital createFromDto(HospitalDto dto) {
		Hospital hospital = new Hospital();
		hospital.id = dto.getId();
		hospital.name = dto.getName();
		hospital.description = dto.getDescription();
		return hospital;
	}
}
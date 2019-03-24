package gestao.model.hospital;

import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	private Integer maximumNumberOfBeds;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hospital")
	private List<Patient> patients = new ArrayList<>();

	@OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade=CascadeType.ALL)
	private Address address;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hospital")
	private List<ProductItem> stock = new ArrayList<>();

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
}

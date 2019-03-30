package gestao.model.product.bloodbank;

import gestao.model.product.Product;

import javax.persistence.*;

@Entity
public class BloodBank extends Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private BloodType bloodType;


	public BloodBank(String name, String description, BloodType bloodType) {
		super(name, description);
		this.bloodType = bloodType;
	}
}

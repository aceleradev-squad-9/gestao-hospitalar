package gestao.model.product.bloodbank;

import gestao.model.product.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BloodBank extends Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;

	public BloodBank(String name, String description, String type) {
		super(name, description);
		this.type = type;
	}
}

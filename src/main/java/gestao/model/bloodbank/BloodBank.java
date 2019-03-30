package gestao.model.bloodbank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class BloodBank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@NotNull(message = "O tipo sanguíneo não foi informado.")
	private BloodType bloodType;


	BloodBank() {
	}

	public BloodBank(BloodType bloodType) {
		this.bloodType = bloodType;
	}

	public Long getId() {
		return id;
	}

	public BloodType getBloodType() {
		return bloodType;
	}
}

package gestao.model.bloodbank;

import gestao.model.product.BloodType;

public class BloodBank {

	private Long id;

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

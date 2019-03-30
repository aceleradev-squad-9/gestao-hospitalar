package gestao.model.bloodbank;

import javax.persistence.*;

@Entity
public class BloodBank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private BloodType bloodType;

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

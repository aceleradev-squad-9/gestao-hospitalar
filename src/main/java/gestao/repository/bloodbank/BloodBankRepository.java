package gestao.repository.bloodbank;

import gestao.model.bloodbank.BloodBank;
import gestao.model.bloodbank.BloodType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodBankRepository extends CrudRepository<BloodBank, Long> {

    Boolean existsByBloodType(BloodType bloodType);
}

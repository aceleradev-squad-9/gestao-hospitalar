package gestao.repository.product;

import gestao.model.product.bloodbank.BloodBank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodBankRepository extends CrudRepository<BloodBank, Long> {
}

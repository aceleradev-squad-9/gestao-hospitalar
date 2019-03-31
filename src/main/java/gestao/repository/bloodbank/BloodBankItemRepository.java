package gestao.repository.bloodbank;

import gestao.model.bloodbank.BloodBankItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodBankItemRepository extends CrudRepository<BloodBankItem, Long> {
}

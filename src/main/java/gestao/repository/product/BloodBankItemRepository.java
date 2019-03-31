package gestao.repository.product;

import gestao.model.product.BloodBankItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodBankItemRepository extends CrudRepository<BloodBankItem, Long> {
}

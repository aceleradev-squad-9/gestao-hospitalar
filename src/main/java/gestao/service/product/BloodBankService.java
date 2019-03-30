package gestao.service.product;

import gestao.model.product.bloodbank.BloodBank;
import gestao.repository.product.BloodBankRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BloodBankService {

    @Autowired
    private BloodBankRepository bloodBankRepository;


    public BloodBank create(BloodBank bloodBank) {
        return this.bloodBankRepository.save(bloodBank);
    }
}

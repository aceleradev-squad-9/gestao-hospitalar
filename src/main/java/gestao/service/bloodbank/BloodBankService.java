package gestao.service.bloodbank;

import gestao.exception.bloodbank.BloodBankNotFoundException;
import gestao.exception.bloodbank.BloodBankWithSameBloodTypeException;
import gestao.model.bloodbank.BloodBank;
import gestao.model.bloodbank.BloodType;
import gestao.repository.bloodbank.BloodBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloodBankService {

    @Autowired
    private BloodBankRepository bloodBankRepository;


    public BloodBank create(BloodBank bloodBank) {
        if(this.existsByBloodType(bloodBank.getBloodType())){
            throw new BloodBankWithSameBloodTypeException();
        }
        return this.bloodBankRepository.save(bloodBank);
    }

    public Iterable<BloodBank> find() {
        return this.bloodBankRepository.findAll();
    }

    public BloodBank findById(Long id) {
        return this.bloodBankRepository.findById(id).orElseThrow(() -> new BloodBankNotFoundException());
    }

    private Boolean existsById(Long id) {
        return this.bloodBankRepository.existsById(id);
    }

    private Boolean existsByBloodType(BloodType bloodType) {
        return this.bloodBankRepository.existsByBloodType(bloodType);
    }

    public BloodBank update(BloodBank bloodBank) {
        if (!this.existsById(bloodBank.getId())) {
            throw new BloodBankNotFoundException();
        }
        if(this.existsByBloodType(bloodBank.getBloodType())){
            throw new BloodBankWithSameBloodTypeException();
        }
        return this.bloodBankRepository.save(bloodBank);
    }

}

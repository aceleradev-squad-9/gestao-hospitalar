package gestao.model.product;

import gestao.model.hospital.Hospital;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SQLDelete(sql = "UPDATE BloodBankHospital SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class BloodBankItem extends ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime dateDonation;

    BloodBankItem(){

    }

    public BloodBankItem(Hospital hospital, Product product, Integer amount, LocalDateTime dateDonation) {
        super(hospital, product, amount);
        this.dateDonation = dateDonation;
    }


    public Long getId() {
        return id;
    }

    public LocalDateTime getDateDonation() {
        return dateDonation;
    }

    public static class BloodBankItemBuilder extends ProductItemBuilder {

        private LocalDateTime dateDonation;

        public BloodBankItemBuilder withDateDonation(LocalDateTime dateDonation) {
            this.dateDonation = dateDonation;
            return this;
        }

        public BloodBankItem build() {
            return new BloodBankItem();
        }
    }

    public static BloodBankItemBuilder builder() {
        return new BloodBankItemBuilder();
    }

    public BloodBankItemDto convertToBloodBankItemDto() {

        BloodBankItemDto bloodBankItemDto = new BloodBankItemDto();
        bloodBankItemDto.setAmount(this.getAmount());
        bloodBankItemDto.setDateDonation(this.getDateDonation());

        return bloodBankItemDto;
    }
}

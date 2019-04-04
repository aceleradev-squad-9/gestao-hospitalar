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

    @Column
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    BloodBankItem(){
    }

    public BloodBankItem(Hospital hospital, Product product, Integer amount, LocalDateTime dateDonation, BloodType bloodType) {
        super(hospital, product, amount);
        this.dateDonation = dateDonation;
        this.bloodType = bloodType;
    }


    public Long getId() {
        return id;
    }

    public LocalDateTime getDateDonation() {
        return dateDonation;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public static class BloodBankItemBuilder extends ProductItemBuilder {

        private LocalDateTime dateDonation;

        private BloodType bloodType;

        public BloodBankItemBuilder withDateDonation(LocalDateTime dateDonation) {
            this.dateDonation = dateDonation;
            return this;
        }

        public BloodBankItemBuilder withBloodType(BloodType bloodType) {
            this.bloodType = bloodType;
            return this;
        }

        public BloodBankItem build() {
            return new BloodBankItem(super.build().getHospital(), super.build().getProduct(), super.build().getAmount(), dateDonation, bloodType);
        }
    }

    public static BloodBankItemBuilder builder() {
        return new BloodBankItemBuilder();
    }

    public BloodBankItemDto convertToBloodBankItemDto() {

        BloodBankItemDto bloodBankItemDto = new BloodBankItemDto();
        bloodBankItemDto.setAmount(this.getAmount());
        bloodBankItemDto.setDateDonation(this.getDateDonation());
        bloodBankItemDto.setBloodType(this.getBloodType());

        return bloodBankItemDto;
    }
}

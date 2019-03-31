package gestao.model.product;

import gestao.model.bloodbank.BloodBank;
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

//    @Column
//    private Integer amount;

    @Column
    private LocalDateTime dateDonation;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private BloodBank bloodBank;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Hospital hospital;

    public BloodBankItem(Hospital hospital, Product product, Integer amount, LocalDateTime dateDonation) {
        super(hospital, product, amount);
        this.dateDonation = dateDonation;
    }


//    public Long getId() {
//        return id;
//    }
//
//    public Integer getAmount() {
//        return amount;
//    }
//
//    public LocalDateTime getDateDonation() {
//        return dateDonation;
//    }
//
//    public BloodBank getBloodBank() {
//        return bloodBank;
//    }
//
//    public Hospital getHospital() {
//        return hospital;
//    }
//
//    public static class BloodBankItemBuilder extends ProductItemBuilder {
//
//        private Integer amount;
//
//        private LocalDateTime dateDonation;
//
//        private BloodBank bloodBank;
//
//        private Hospital hospital;
//
//        public BloodBankItemBuilder withAmount(Integer amount) {
//            this.amount = amount;
//            return this;
//        }
//
//        public BloodBankItemBuilder withDateDonation(LocalDateTime dateDonation) {
//            this.dateDonation = dateDonation;
//            return this;
//        }
//
//        public BloodBankItemBuilder withBloodBank(BloodBank bloodBank) {
//            this.bloodBank = bloodBank;
//            return this;
//        }
//
//        public BloodBankItemBuilder withHospital(Hospital hospital) {
//            this.hospital = hospital;
//            return this;
//        }
//
//        public BloodBankItem build() {
//            return new BloodBankItem(amount, dateDonation, bloodBank, hospital);
//        }
//    }
//
//    public static BloodBankItemBuilder builder() {
//        return new BloodBankItemBuilder();
//    }
}

package gestao.model.bloodbank;

import gestao.model.BaseEntity;
import gestao.model.hospital.Hospital;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SQLDelete(sql = "UPDATE BloodBankHospital SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class BloodBankItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer amount;

    @Column
    private LocalDateTime dateDonation;

    @ManyToOne(fetch = FetchType.LAZY)
    private BloodBank bloodBank;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hospital hospital;


}

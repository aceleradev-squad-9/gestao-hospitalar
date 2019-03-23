package gestao.model.address;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Address {
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String street;
    
    private String district;

    private String city;
    
    private String state;

    private String cep;

    private String number;

    private double latitude;

    private double longitude;
}

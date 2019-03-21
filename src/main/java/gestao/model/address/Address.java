package gestao.model.address;

import org.springframework.data.annotation.Id;

public class Address {
    @Id
    private String id;

    private String street;
    
    private String district;

    private String city;
    
    private String state;

    private String cep;

    private String number;

    private Double latitude;

    private Double longitude;
}

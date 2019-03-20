package gestao.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pessoa")
public class Pessoa {

    @Id
    private String id;

    private String name;
    
    private String phoneNumber;
    
    private Address address;
}

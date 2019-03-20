package gestao.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="blood_bank")
public class BloodBank extends Stock {
	
	private Integer id;
	
	private String type;
	
	private Integer amount;

}

package gestao.model.stock;

import org.springframework.data.annotation.Id;

import gestao.model.hospital.Hospital;

public class Stock {
    
	@Id
    private String id;
	
	private Hospital hospital;

}

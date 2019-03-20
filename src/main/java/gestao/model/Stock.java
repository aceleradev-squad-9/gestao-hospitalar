package gestao.model;

import org.springframework.data.annotation.Id;

public class Stock {
    
	@Id
    private String id;
	
	private Hospital hospital;

}

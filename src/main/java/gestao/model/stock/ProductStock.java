package gestao.model.stock;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="product_stock")
public class ProductStock extends Stock {
	
	private Integer id;
	
	private String description;
	
	private Integer amount;

}

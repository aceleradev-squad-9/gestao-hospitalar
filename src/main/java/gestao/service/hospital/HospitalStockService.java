package gestao.service.hospital;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestao.exception.hospital.NearestHospitalNotFoundException;
import gestao.model.hospital.Hospital;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;

@Service
public class HospitalStockService {
  @Autowired
  private HospitalService hospitalService;
  
  public ProductItem addProductInStock(Long hospitalId, Product product, Integer amount) {

		Hospital hospital = this.hospitalService.findById(hospitalId);
		
		ProductItem productItem = hospital.addProductInStock(product, amount);

		this.hospitalService.save(hospital);

		return productItem;
  }
  
  public ProductItem findProductInStock(Long hospitalId, Product product) {
		Hospital hospital = this.hospitalService.findById(hospitalId);
		return hospital.findProductInStock(product);
  }
  
  public ProductItem transferProductItemFromTheFirstAbleHospital(
		List<Hospital> hospitals,
		Hospital hospitalNeedingTransfer, 
		Product product, 
		Integer amount
	) {
		Hospital nearestHospital = hospitals.stream()
			.filter((hospital) -> hospital.reduceStock(product, amount))
			.findFirst()
			.orElseThrow(NearestHospitalNotFoundException::new);

		ProductItem productItem = hospitalNeedingTransfer.addProductInStock(product, amount);

		this.hospitalService.save(hospitalNeedingTransfer);
		this.hospitalService.save(nearestHospital);

		return productItem;
	}
}
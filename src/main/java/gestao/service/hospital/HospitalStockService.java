package gestao.service.hospital;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gestao.exception.hospital.NoHospitalAbleToTransferProductException;
import gestao.model.hospital.Hospital;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.model.product.ProductItemDto;
import gestao.service.product.ProductItemService;

@Service
public class HospitalStockService {
  @Autowired
	private HospitalService hospitalService;
	
	@Autowired
	private ProductItemService productItemService;
  
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
	
	public Page<ProductItemDto> findAllHospitalProductItems(
		Long hospitalId,
		Pageable pageable
	){
		return this.productItemService.findAllHospitalProductItems(
			hospitalId, 
			pageable
		);
	}
	
	@Transactional
  public ProductItem transferProductItemFromTheFirstAbleHospital(
		List<Hospital> hospitals,
		Hospital hospitalNeedingTransfer, 
		Product product, 
		Integer amount
	) {
		Hospital hospitalAbleToTransfer = hospitals.stream()
			.filter((hospital) -> 
				productItemService.checkIfHospitalIsAbleToTransferProductItems(
					hospital,
					product,
					amount,
					Hospital.MIN_STOCK_AMOUNT
				)
			)
			.findFirst()
			.orElseThrow(NoHospitalAbleToTransferProductException::new);

		this.productItemService.reduceAmountOfItems(hospitalAbleToTransfer, product, amount);

		ProductItem productItem = hospitalNeedingTransfer.addProductInStock(product, amount);
		this.hospitalService.save(hospitalNeedingTransfer);

		return productItem;
	}
}

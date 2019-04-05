package gestao.controller.hospital;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gestao.model.hospital.Hospital;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.model.product.ProductItemDto;
import gestao.service.hospital.HospitalService;
import gestao.service.hospital.HospitalStockService;
import gestao.service.product.ProductItemService;
import gestao.service.product.ProductService;

@RestController
@RequestMapping("/hospital/{hospitalId}/stock")
public class HospitalStockController {
	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private HospitalStockService hospitalStockService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductItemService productItemService;

	@PutMapping("/{productId}")
	public ProductItemDto addProductInStock(@PathVariable Long hospitalId, @PathVariable Long productId,
			@RequestBody @Valid ProductItemDto productItemDto) {

		Product product = this.productService.findById(productId);

		ProductItem productItem = this.hospitalStockService
			.addProductInStock(
				hospitalId, 
				product,
				productItemDto.getAmount()
			);

		return productItem.convertToDto();
	}

	@GetMapping(params = {"page", "size"})
	public Page<ProductItemDto> findStockProducts(
		@RequestParam int page,
		@RequestParam int size,
		@PathVariable Long hospitalId
	) {
		return this.productItemService.findAllHospitalProductItems(
			hospitalId, 
			PageRequest.of(page, size)
		);
	}

	@GetMapping("/{productId}")
	public ProductItemDto findStockProduct(@PathVariable Long hospitalId, @PathVariable Long productId) {
		Product product = productService.findById(productId);
		return hospitalStockService.findProductInStock(hospitalId, product).convertToDto();
	}

	@PutMapping("/order/{productId}")
	public ProductItemDto orderProduct(
		@PathVariable("hospitalId") Long hospitalId,
		@PathVariable("productId") Long productId, 
		@RequestBody @Valid ProductItemDto productItemDto
	) {

		Hospital hospital = this.hospitalService.findById(hospitalId);
		
		List<Hospital> hospitals = this.hospitalService.findNearestHospitals(hospital);

		Product product = this.productService.findById(productId);
		
		ProductItem productItem = this.hospitalStockService
			.transferProductItemFromTheFirstAbleHospital(
				hospitals, 
				hospital,
				product,
				productItemDto.getAmount()
			);

		return productItem.convertToDto();

	}
}
package gestao.controller.hospital;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gestao.model.hospital.Hospital;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.model.product.dto.ProductAmountDto;
import gestao.model.product.dto.ProductItemDto;
import gestao.service.hospital.HospitalService;
import gestao.service.hospital.HospitalStockService;
import gestao.service.product.ProductService;

@RestController
@RequestMapping("/hospital/")
public class HospitalStockController {
	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private HospitalStockService hospitalStockService;

	@Autowired
	private ProductService productService;

	@PutMapping("/{hospitalId}/stock/{productId}")
	public ProductItemDto addProductInStock(@PathVariable Long hospitalId, @PathVariable Long productId,
			@RequestBody @Valid ProductItemDto productItemDto) {

		Product product = this.productService.findById(productId);

		ProductItem productItem = this.hospitalStockService.addProductInStock(hospitalId, product, productItemDto);

		return productItem.convertToDto();
	}

	@PutMapping("/stock/items/{productItemId}")
	public ProductItemDto reduceProductInStock(@PathVariable Long productItemId,
			@RequestBody @Valid ProductItemDto productItemDto) {

		ProductItem productItem = this.hospitalStockService.reduceProductInStock(productItemId,
				productItemDto.getAmount());

		return productItem.convertToDto();
	}

	@DeleteMapping("/stock/items/{productItemId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteProductInStock(@PathVariable Long productItemId) {
		this.hospitalStockService.deleteProductInStock(productItemId);
	}

	@GetMapping(value="/{hospitalId}/stock", params = { "page", "size" })
	public Page<ProductItemDto> findStockProducts(@RequestParam int page, @RequestParam int size,
			@PathVariable Long hospitalId) {
		this.hospitalService.verifyIfExistsById(hospitalId);
		return this.hospitalStockService.findAllHospitalProductItems(hospitalId, PageRequest.of(page, size));
	}

	@GetMapping(value = "/{hospitalId}/stock/{productId}", params = { "page", "size" })
	public Page<ProductItemDto> findStockProducts(@RequestParam int page, @RequestParam int size,
			@PathVariable Long hospitalId, @PathVariable Long productId) {
		Product product = productService.findById(productId);
		return hospitalStockService.findProductInStock(hospitalId, product, PageRequest.of(page, size))
				.map(ProductItem::convertToDto);
	}

	@PutMapping("/{hospitalId}/order/{productId}")
	public ProductItemDto orderProduct(@PathVariable("hospitalId") Long hospitalId,
			@PathVariable("productId") Long productId, @RequestBody @Valid ProductAmountDto productAmountDto) {

		Hospital hospital = this.hospitalService.findById(hospitalId);

		List<Hospital> hospitals = this.hospitalService.findNearestHospitals(hospital);

		Product product = this.productService.findById(productId);

		ProductItem productItem = this.hospitalStockService.transferProductItemFromTheFirstAbleHospital(hospitals,
				hospital, product, productAmountDto.getAmount());

		return productItem.convertToDto();

	}
}

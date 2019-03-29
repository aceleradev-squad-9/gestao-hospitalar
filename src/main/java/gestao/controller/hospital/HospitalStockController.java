package gestao.controller.hospital;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gestao.model.hospital.Hospital;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.model.product.ProductItemDto;
import gestao.service.hospital.HospitalService;
import gestao.service.product.ProductService;

@RestController
@RequestMapping("/hospital/{hospitalId}/stock")
public class HospitalStockController {
	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private ProductService productService;

	@PutMapping("/{productId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ProductItemDto addProductInStock(@PathVariable Long hospitalId, @PathVariable Long productId,
			@RequestBody @Valid ProductItemDto productItemDto) {

		Hospital hospital = hospitalService.findById(hospitalId);
		Product product = this.productService.findById(productId);

		ProductItem productItem = hospital.addProductInStock(product, productItemDto.getAmount());

		hospitalService.save(hospital);

		return productItem.convertToDto();
	}

	@GetMapping("")
	public List<ProductItemDto> findStockProducts(@PathVariable Long hospitalId) {
		return hospitalService.findById(hospitalId).getStock().stream().map(ProductItem::convertToDto)
				.collect(toList());
	}

	@GetMapping("/{productId}")
	public ProductItemDto findStockProduct(@PathVariable Long hospitalId, @PathVariable Long productId) {

		Hospital hospital = hospitalService.findById(hospitalId);
		Product product = productService.findById(productId);

		return hospital.findProductInStock(product).convertToDto();
	}

	@PutMapping("/order/{productId}")
	public ProductItemDto orderProduct(@PathVariable("hospitalId") Long hospitalId,
			@PathVariable("productId") Long productId, @RequestBody @Valid ProductItemDto productItemDto) {

		Product product = this.productService.findById(productId);

		ProductItem productItem = this.hospitalService.orderProductFromNearestHospitals(hospitalId, product,
				productItemDto.getAmount());

		return productItem.convertToDto();

	}
}
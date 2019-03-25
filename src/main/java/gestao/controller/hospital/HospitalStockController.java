package gestao.controller.hospital;

import java.util.List;

import javax.validation.Valid;

import static java.util.stream.Collectors.toList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gestao.model.hospital.Hospital;
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
  
  @PostMapping("/{productId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ProductItemDto addProductInStock(@PathVariable Long hospitalId, @PathVariable Long productId,
			@RequestBody @Valid ProductItemDto productItemDto) {

		Hospital hospital = hospitalService.findById(hospitalId);

		ProductItem productItem = productService.createProductItem(productId, productItemDto);

		hospital.addProductInStock(productItem);

		hospitalService.save(hospital);

		return ProductItem.convertToDto(productItem);
	}

	@GetMapping("")
	public List<ProductItemDto> findStockProducts(@PathVariable Long hospitalId) {
		return hospitalService.findById(hospitalId)
			.getStock()
			.stream()
			.map(ProductItem::convertToDto)
			.collect(toList());
	}

	@GetMapping("/{productId}")
	public ProductItemDto findStockProduct(@PathVariable Long hospitalId, @PathVariable Long productId) {

		hospitalService.verifyIfExistsById(hospitalId);

		return ProductItem.convertToDto(
			productService.findProductItemByHospitalAndProduct(productId, hospitalId)
		);
	}
}
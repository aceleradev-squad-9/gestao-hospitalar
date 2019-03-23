package gestao.controller.hospital;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import gestao.model.hospital.HospitalDto;
import gestao.model.product.ProductItem;
import gestao.model.product.ProductItemDto;
import gestao.service.hospital.HospitalService;
import gestao.service.product.ProductService;

@RestController
@RequestMapping("/hospital")
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private ProductService productService;

	@PostMapping("")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Hospital createHospital(@Valid @RequestBody HospitalDto hospitalDto) {
		return hospitalService.createHospital(hospitalDto);
	}

	@GetMapping(value = "/{id}")
	public Hospital findById(@PathVariable("id") Long id) {
		return hospitalService.findById(id);
	}

	@GetMapping
	public Iterable<Hospital> findAll() {
		return hospitalService.findAll();
	}

	@PostMapping("/{hospitalId}/stock/{productId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Hospital addProductInStock(@PathVariable Long hospitalId, @PathVariable Long productId,
			@RequestBody @Valid ProductItemDto productItemDto) {

		Hospital hospital = hospitalService.findById(hospitalId);

		ProductItem productItem = productService.createProductItem(productId, productItemDto);

		hospital.addProductInStock(productItem);

		return hospitalService.save(hospital);
	}

	@GetMapping("/{hospitalId}/stock/")
	@ResponseStatus(code = HttpStatus.CREATED)
	public List<ProductItemDto> findStockProducts(@PathVariable Long hospitalId) {

		Hospital hospital = hospitalService.findById(hospitalId);

		return hospital.getStock().stream().map((productItem) -> ProductItemDto.convertToDto(productItem))
				.collect(Collectors.toList());
	}
	
	@GetMapping("/{hospitalId}/stock/{productId}")
	public ProductItemDto findStockProduct(@PathVariable Long hospitalId, 
			@PathVariable Long productId) {

		return ProductItemDto.convertToDto(productService.findProductItem(productId, hospitalId));
	}
}
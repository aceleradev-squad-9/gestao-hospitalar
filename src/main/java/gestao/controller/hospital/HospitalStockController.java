package gestao.controller.hospital;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Valid;

import gestao.model.product.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import gestao.service.hospital.HospitalService;
import gestao.service.product.ProductService;

@RestController
@RequestMapping("/hospital/{hospitalId}/stock")
public class HospitalStockController {
	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private ProductService productService;


	@PostMapping("/bloodbank/{productId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BloodBankItemDto addBloodBankInStock(@PathVariable Long hospitalId, @PathVariable Long productId,
												@RequestBody @Valid BloodBankItemDto bloodBankItemDto) {

		Product product = this.productService.findById(productId);

		BloodBankItem bloodBankItem = this.hospitalService.addBloodBankInStock(hospitalId, product,
				bloodBankItemDto.getAmount(), bloodBankItemDto.getDateDonation(), bloodBankItemDto.getBloodType());

		return bloodBankItem.convertToBloodBankItemDto();
	}


	@PutMapping("/{productId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ProductItemDto addProductInStock(@PathVariable Long hospitalId, @PathVariable Long productId,
			@RequestBody @Valid ProductItemDto productItemDto) {

		Product product = this.productService.findById(productId);

		ProductItem productItem = this.hospitalService.addProductInStock(hospitalId, product,
				productItemDto.getAmount());

		return productItem.convertToDto();
	}

	@GetMapping("")
	public List<ProductItemDto> findStockProducts(@PathVariable Long hospitalId) {
		return hospitalService.findById(hospitalId).getStock().stream().map(ProductItem::convertToDto)
				.collect(toList());
	}

	@GetMapping("/{productId}")
	public ProductItemDto findStockProduct(@PathVariable Long hospitalId, @PathVariable Long productId) {
		Product product = productService.findById(productId);
		return hospitalService.findProductInStock(hospitalId, product).convertToDto();
	}

	@GetMapping("/bloodbank")
	public List<BloodBankItemDto> findStockBloodBanks(@PathVariable Long hospitalId) {
		return hospitalService.findById(hospitalId).getBloodBank().stream().map(BloodBankItem::convertToBloodBankItemDto)
				.collect(toList());
	}

	@GetMapping("/bloodbank/{productId}")
	public BloodBankItemDto findStockBloodBank(@PathVariable Long hospitalId, @PathVariable Long productId) {
		Product product = productService.findById(productId);
		return hospitalService.findBloodBankInStock(hospitalId, product).convertToBloodBankItemDto();
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
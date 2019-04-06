package gestao.controller.hospital;

import java.util.List;
import static java.util.stream.Collectors.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gestao.model.product.*;
import gestao.model.hospital.Hospital;
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
		return this.hospitalStockService.findAllHospitalProductItems(
			hospitalId, 
			PageRequest.of(page, size)
		);
	}

	@GetMapping("/{productId}")
	public ProductItemDto findStockProduct(@PathVariable Long hospitalId, @PathVariable Long productId) {
		Product product = productService.findById(productId);
		return hospitalStockService.findProductInStock(hospitalId, product).convertToDto();
	}

	@PostMapping("/bloodbank/{productId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BloodBankItemDto addBloodBankInStock(@PathVariable Long hospitalId, @PathVariable Long productId,
												@RequestBody @Valid BloodBankItemDto bloodBankItemDto) {

		Product product = this.productService.findById(productId);

		BloodBankItem bloodBankItem = this.hospitalStockService.addBloodBankInStock(hospitalId, product,
				bloodBankItemDto.getAmount(), bloodBankItemDto.getDateDonation(), bloodBankItemDto.getBloodType());

		return bloodBankItem.convertToBloodBankItemDto();
	}

	@GetMapping("/bloodbank")
	public List<BloodBankItemDto> findStockBloodBanks(@PathVariable Long hospitalId) {
		return hospitalService.findById(hospitalId)
			.getBloodBank()
			.stream()
			.map(BloodBankItem::convertToBloodBankItemDto)
			.collect(toList());
	}

	@GetMapping("/bloodbank/{productId}")
	public BloodBankItemDto findStockBloodBank(@PathVariable Long hospitalId, @PathVariable Long productId) {
		Product product = productService.findById(productId);
		return hospitalStockService.findBloodBankInStock(hospitalId, product).convertToBloodBankItemDto();
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
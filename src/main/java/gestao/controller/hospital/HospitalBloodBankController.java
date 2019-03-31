package gestao.controller.hospital;

import gestao.model.bloodbank.BloodBank;
import gestao.model.product.BloodBankItem;
import gestao.service.hospital.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/hospital/{hospitalId}/bloodbank")
public class HospitalBloodBankController {
//	@Autowired
//	private HospitalService hospitalService;

//	@Autowired
//	private BloodBankService bloodBankService;

//	@PostMapping
//	@ResponseStatus(code = HttpStatus.CREATED)
//	public BloodBankItem addBloodBank(@PathVariable Long hospitalId,
//										   @RequestBody @Valid BloodBankItem bloodBankItem) {
//		System.out.println(bloodBankItem);
//		// obtem id do tipo de banco de sangue a ser inserido
//		BloodBank bloodBank = this.bloodBankService.findById(bloodBankItem.getBloodBank().getId());
//
////		ProductItem productItem = this.hospitalService.addProductInStock(hospitalId, product,
////				productItemDto.getAmount());
//
////		return productItem.convertToDto();
//		return null;
//	}
//
//	@GetMapping("")
//	public List<ProductItemDto> findStockProducts(@PathVariable Long hospitalId) {
//		return hospitalService.findById(hospitalId).getStock().stream().map(ProductItem::convertToDto)
//				.collect(toList());
//	}
//
//	@GetMapping("/{productId}")
//	public ProductItemDto findStockProduct(@PathVariable Long hospitalId, @PathVariable Long productId) {
//		Product product = productService.findById(productId);
//		return hospitalService.findProductInStock(hospitalId, product).convertToDto();
//	}
//
//	@PutMapping("/order/{productId}")
//	public ProductItemDto orderProduct(@PathVariable("hospitalId") Long hospitalId,
//			@PathVariable("productId") Long productId, @RequestBody @Valid ProductItemDto productItemDto) {
//
//		Product product = this.productService.findById(productId);
//
//		ProductItem productItem = this.hospitalService.orderProductFromNearestHospitals(hospitalId, product,
//				productItemDto.getAmount());
//
//		return productItem.convertToDto();
//
//	}
}
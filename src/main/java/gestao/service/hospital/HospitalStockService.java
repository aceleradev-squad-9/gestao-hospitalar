package gestao.service.hospital;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gestao.exception.hospital.NoHospitalAbleToTransferProductException;
import gestao.model.hospital.Hospital;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.model.product.dto.ProductItemDto;
import gestao.service.product.ProductItemService;

@Service
public class HospitalStockService {
	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private ProductItemService productItemService;

	public ProductItem addProductInStock(Long hospitalId, Product product, ProductItemDto productItemDto) {
		Hospital hospital = this.hospitalService.findById(hospitalId);

		return this.addProductInStock(hospital, product, productItemDto.getAmount(),
				productItemDto.getExpirationDate());
	}

	private ProductItem addProductInStock(Hospital hospital, Product product, Integer amount,
			LocalDate expirationDate) {

		ProductItem productItem = this.findProductInStock(hospital, product, expirationDate).orElse(null);

		if (productItem != null) {
			productItem.increaseAmount(amount);
		} else {
			productItem = hospital.addProductInStock(product, amount, expirationDate);
		}
	
		this.productItemService.save(productItem);

		return productItem;
	}

	public ProductItem reduceProductInStock(Long productItemId, Integer amount) {
		ProductItem productItem = this.productItemService.findById(productItemId);
		productItem.reduceAmount(amount);
		this.productItemService.save(productItem);
		return productItem;
	}

	public void deleteProductInStock(Long productItemId) {
		this.productItemService.delete(this.productItemService.findById(productItemId));
	}

	public Page<ProductItem> findProductInStock(Long hospitalId, Product product, Pageable pageable) {
		Hospital hospital = this.hospitalService.findById(hospitalId);
		return this.productItemService.findProductItems(hospital, product, pageable);
	}

	private Optional<ProductItem> findProductInStock(Hospital hospital, Product product, LocalDate expirationDate) {
		return this.productItemService.findProductItem(hospital, product, expirationDate);
	}

	public Page<ProductItemDto> findAllHospitalProductItems(Long hospitalId, Pageable pageable) {
		return this.productItemService.findAllHospitalProductItems(hospitalId, pageable);
	}

	@Transactional
	public ProductItem transferProductItemFromTheFirstAbleHospital(List<Hospital> hospitals,
			Hospital hospitalNeedingTransfer, Product product, Integer amount) {

		ProductItem productFromhospitalAbleToTransfer = hospitals.stream()
				.map((hospital) -> productItemService.findProductItemAbleToBeTransferedFromHospital(
						hospital,
						product,
						amount,
						Hospital.MIN_STOCK_AMOUNT
					)
				)
				.filter(Objects::nonNull)
				.findFirst().orElseThrow(NoHospitalAbleToTransferProductException::new);

		this.productItemService.reduceAmountOfItems(productFromhospitalAbleToTransfer.getId(), amount);

		return this.addProductInStock(hospitalNeedingTransfer, product, amount,
				productFromhospitalAbleToTransfer.getExpirationDate());
	}
}

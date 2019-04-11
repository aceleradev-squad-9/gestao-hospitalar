package gestao.service.product;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gestao.exception.hospital.ProductNotFoundInHospitalStockException;
import gestao.model.hospital.Hospital;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.model.product.dto.ProductItemDto;
import gestao.repository.product.ProductItemRepository;

@Service
public class ProductItemService {

	@Autowired
	private ProductItemRepository productItemRepository;

	public ProductItem save(ProductItem productItem) {
		return productItemRepository.save(productItem);
	}

	public Page<ProductItemDto> findAllHospitalProductItems(Long hospitalId, Pageable pageable) {
		return this.productItemRepository.findAllByHospitalId(hospitalId, pageable).map(ProductItem::convertToDto);
	}

	public ProductItem findProductItemAbleToBeTransferedFromHospital(Hospital hospital, Product product, Integer amount,
			Integer minAmountAHospitalShouldHave) {

		ProductItem productItem = null;

		Page<ProductItem> pageList = this.productItemRepository.checkIfAHospitalIsAbleToTransferItemsOfAProduct(
				hospital.getId(), product.getId(), amount, minAmountAHospitalShouldHave,
				PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "expirationDate")));

		if (pageList.getTotalElements() > 0) {
			productItem = pageList.getContent().stream().filter(Objects::nonNull).findFirst().orElseGet(null);
		}

		return productItem;
	}

	public void reduceAmountOfItems(Long productItemId, Integer amount) {
		this.productItemRepository.reduceAmountOfProductItem(productItemId, amount);
	}

	public Page<ProductItem> findProductItems(Hospital hospital, Product product, Pageable pageable) {
		return this.productItemRepository.findByHospitalAndProduct(hospital, product, pageable);
	}

	public Optional<ProductItem> findProductItem(Hospital hospital, Product product, LocalDate expirationDate) {
		return this.productItemRepository.findByHospitalAndProductAndExpirationDate(hospital, product, expirationDate);
	}

	public Boolean verifyIfProductItemExists(Hospital hospital, Product product, LocalDate expirationDate) {
		return this.verifyIfProductItemExists(hospital, product, expirationDate);
	}

	public ProductItem findById(Long id) {
		return this.productItemRepository.findById(id).orElseThrow(ProductNotFoundInHospitalStockException::new);
	}

	public void delete(ProductItem productItem) {
		this.productItemRepository.delete(productItem);
	}
}
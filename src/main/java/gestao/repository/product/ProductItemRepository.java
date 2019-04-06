package gestao.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import gestao.model.product.Product;
import gestao.model.product.ProductItem;

/**
 * Interface responsável pelo repositório de dados de {@link ProductItem}.
 * 
 * @author edmilson.santana
 *
 */
@Repository
public interface ProductItemRepository extends PagingAndSortingRepository<ProductItem, Long> {

	@Modifying
	@Query("UPDATE ProductItem SET deleted = true WHERE product = ?1")
	void deleteAllByProduct(Product product);

	Page<ProductItem> findAllByHospitalId(Long hospitalId, Pageable pageable);

	@Query(
		"SELECT p FROM ProductItem p WHERE p.hospital.id = ?1 AND p.product.id = ?2 AND p.amount-?3 > ?4"
	)
	ProductItem checkIfAHospitalIsAbleToTransferItemsOfAProduct(
		Long hospitalId,
		Long productId,
		Integer amount,
		Integer minAmountOfItemsAHospitalMustHave
	);

	@Modifying
	@Query(
		"UPDATE ProductItem p SET p.amount = p.amount - ?3 WHERE p.hospital.id = ?1 AND p.product.id = ?2"
	)
	void reduceAmountOfItemsFromAProduct(
		Long hospitalId,
		Long productId,
		Integer amount
	);
}

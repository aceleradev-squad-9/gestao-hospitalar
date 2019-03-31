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
}

package gestao.repository.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gestao.model.product.ProductItem;

/**
 * Interface responsável pelo repositório de dados de {@link ProductItem}
 * 
 * @author edmilson.santana
 *
 */
@Repository
public interface ProductItemRepository extends CrudRepository<ProductItem, Long> {

	@Query("SELECT pi FROM ProductItem as pi where hospital.id = :hospitalId and product.deleted = false")
	List<ProductItem> findProductItemByHospital(@Param("hospitalId") Long hospitalId);
	
	@Query("SELECT pi FROM ProductItem as pi where product.id = :productId and hospital.id = :hospitalId and product.deleted = false")
	Optional<ProductItem> findProductItemByHospitalAndProduct(@Param("productId") Long productId, @Param("hospitalId") Long hospitalId);
}

package gestao.repository.product;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
public interface ProductItemRepository extends CrudRepository<ProductItem, Long> {

	@Modifying
	@Query("UPDATE ProductItem SET deleted = true WHERE product = ?1")
	void deleteAllByProduct(Product product);
}

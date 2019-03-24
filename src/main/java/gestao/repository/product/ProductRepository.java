package gestao.repository.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gestao.model.product.Product;

/**
 * Interface responsável pelo repositório de dados de {@link Product}
 * 
 * @author edmilson.santana
 *
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

}

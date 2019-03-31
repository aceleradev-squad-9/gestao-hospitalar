package gestao.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gestao.exception.product.ProductNotFoundException;
import gestao.model.product.Product;
import gestao.repository.product.ProductItemRepository;
import gestao.repository.product.ProductRepository;

/**
 * Classe responsável pela implementação dos serviços de produto.
 * 
 * @author edmilson.santana
 *
 */
@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductItemRepository productItemRepository;

	/**
	 * Método responsável por criar um produto.
	 * 
	 * @param product - {@link Product}
	 * @return produto criado - {@link Product}
	 */
	public Product create(Product product) {

		return this.productRepository.save(product);
	}

	/**
	 * Método responsável por obter uma lista de produtos.
	 * 
	 * @return lista de produtos - {@link List}
	 */
	public Iterable<Product> find() {
		return this.productRepository.findAll();
	}

	public Page<Product> find(Pageable pageable){
		return this.productRepository.findAll(pageable);
	}

	/**
	 * Método responsável por obter um produto a partir do seu identificador único.
	 * Caso o produto não seja encontrado, o método lança um
	 * {@link ProductNotFoundException}.
	 * 
	 * @param id - {@link Long}
	 * @return produto - {@link Product}
	 */
	public Product findById(Long id) {
		return this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException());
	}

	/**
	 * Método responsável por verificar se um produto existe utilizando o seu
	 * identificador único.
	 * 
	 * @param id - {@link Long}
	 * @return retorna true, caso o produto exista e false, caso não exista. -
	 *         {@link Boolean}
	 */
	private Boolean existsById(Long id) {
		return this.productRepository.existsById(id);
	}

	/**
	 * Método responsável por atualizar um produto. Caso o produto não seja
	 * encontrado, o método lança um {@link ProductNotFoundException}.
	 * 
	 * @param id - {@link Long}
	 * @param product - {@link Product}
	 * @return produto atualizado - {@link Product}
	 */
	public Product update(Long id, Product product) {

		if (!this.existsById(id)) {
			throw new ProductNotFoundException();
		}

		return this.productRepository.save(product);
	}

	/**
	 * Método responsável por remover um produto e os seus itens associados, a
	 * partir do identificador único do produto. Caso o produto não seja encontrada,
	 * o método lança um {@link ProductNotFoundException}.
	 * 
	 * @param id - {@link Long}
	 */
	@Transactional
	public void delete(Long id) {
		Product product = this.findById(id);
		this.productRepository.delete(product);
		this.productItemRepository.deleteAllByProduct(product);
	}
}

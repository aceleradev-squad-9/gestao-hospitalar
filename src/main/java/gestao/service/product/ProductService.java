package gestao.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestao.exception.product.ProductNotFoundException;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.model.product.ProductItemDto;
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
	 * Método responsável por criar um item do produto, com sua respectiva
	 * quantidade e produto relacionado.
	 * 
	 * @param productId      - {@link Long}
	 * @param productItemDto - {@link ProductItemDto}
	 * @return item do produto - {@link ProductItem}
	 */
	public ProductItem createProductItem(Long productId, ProductItemDto productItemDto) {

		return new ProductItem(this.find(productId), productItemDto.getAmount());
	}

	/**
	 * Método responsável por realizar a busca de um item de produto para um
	 * determinado hospital.
	 * 
	 * @param productId  - {@link Long}
	 * @param hospitalId - {@link Long}
	 * @return item de produto - {@link List}
	 */
	public ProductItem findProductItem(Long productId, Long hospitalId) {
		return this.productItemRepository.findProductItem(productId, hospitalId)
				.orElseThrow(() -> new ProductNotFoundException());
	}

	/**
	 * Método responsável por obter uma lista de produtos.
	 * 
	 * @return lista de produtos - {@link List}
	 */
	public Iterable<Product> find() {
		return this.productRepository.findAll();
	}

	/**
	 * Método responsável por obter um produto a partir do seu identificador único.
	 * Caso o produto não seja encontrado, o método lança um
	 * {@link ProductNotFoundException}.
	 * 
	 * @param id - {@link Long}
	 * @return produto - {@link Product}
	 */
	public Product find(Long id) {
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
	 * @param product - {@link Product}
	 * @return produto atualizado - {@link Product}
	 */
	public Product update(Product product) {

		if (!this.existsById(product.getId())) {
			throw new ProductNotFoundException();
		}

		return this.productRepository.save(product);
	}

	/**
	 * Método responsável por remover um produto a partir do seu identificador
	 * único. Caso a pessoa não seja encontrada, o método lança um
	 * {@link ProductNotFoundException}.
	 * 
	 * @param id - {@link Long}
	 */
	public void delete(Long id) {
		this.productRepository.delete(this.find(id));
	}
}

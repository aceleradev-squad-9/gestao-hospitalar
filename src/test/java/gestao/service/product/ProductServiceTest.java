package gestao.service.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import gestao.exception.product.ProductNotFoundException;
import gestao.model.product.Product;
import gestao.repository.product.ProductItemRepository;
import gestao.repository.product.ProductRepository;

@SpringBootTest
public class ProductServiceTest {

	@MockBean
	private ProductRepository productRepository;

	@MockBean
	private ProductItemRepository productItemRepository;

	@InjectMocks
	@Autowired
	private ProductService service;

	@Test
	@DisplayName("Deve criar um novo produto")
	public void shouldCreateProduct() {
		Product product = Product.builder().withName("Produto").withDescription("Descrição").build();

		Product expectedProduct = Product.builder().withId(1L).withName(product.getName())
				.withDescription(product.getDescription()).build();

		when(productRepository.save(product)).thenReturn(expectedProduct);

		Product createdProduct = service.create(product);

		assertEquals(expectedProduct.getId(), createdProduct.getId());
		assertEquals(expectedProduct.getName(), createdProduct.getName());
		assertEquals(expectedProduct.getDescription(), createdProduct.getDescription());
	}

	@Test
	@DisplayName("Deve listar todos os produtos")
	public void shouldFindAllProducts() {

		List<Product> productList = Arrays.asList(
				Product.builder().withId(1L).withName("Produto A").withDescription("Descrição").build(),
				Product.builder().withId(2L).withName("Produto B").withDescription("Descrição").build());

		when(productRepository.findAll()).thenReturn(productList);

		Iterable<Product> obtainedProducts = service.find();

		assertIterableEquals(productList, obtainedProducts);
	}

	@Test
	@DisplayName("Deve buscar um produto a partir do seu identificador único")
	public void shouldFindProduct() {

		Long productId = 1L;

		Product product = Product.builder().withId(productId).withName("Produto").withDescription("Descrição").build();

		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		Product obtainedProduct = service.findById(productId);

		assertEquals(product.getId(), obtainedProduct.getId());
		assertEquals(product.getName(), obtainedProduct.getName());
		assertEquals(product.getDescription(), obtainedProduct.getDescription());
	}

	@Test
	@DisplayName("Deve lançar a exceção ProductNotFoundException caso não encontre um produto buscado pelo seu identificador único")
	public void shouldNotFindProductWhenProductDoesNotExists() {

		Long productId = 1L;

		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class, () -> service.findById(productId));
	}

	@Test
	@DisplayName("Deve atualizar um produto existente")
	public void shouldUpdateProduct() {
		Long productId = 1L;

		Product productWithInfoToUpdate = Product.builder().withName("Produto Atualizado").withDescription("Descrição Atualizada").build();

		Product productToBeUpdate = Product.builder().withId(productId).withName("Produto")
				.withDescription("Descrição").build();

		when(productRepository.findById(productId)).thenReturn(Optional.of(productToBeUpdate));
		when(productRepository.save(productToBeUpdate)).thenReturn(productToBeUpdate);

		Product updatedProduct = service.update(productId, productWithInfoToUpdate);

		assertEquals(productId, updatedProduct.getId());
		assertEquals(productWithInfoToUpdate.getName(), updatedProduct.getName());
		assertEquals(productWithInfoToUpdate.getDescription(), updatedProduct.getDescription());
	}

	@Test
	@DisplayName("Deve lançar a exceção ProductNotFoundException caso tente atualizar um produto que não existe")
	public void shouldNotUpdateProductWhenProductDoesNotExists() {
		Long productId = 1L;

		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class, () -> service.update(productId, Mockito.any()));

		Mockito.verify(productRepository, Mockito.times(0)).save(Mockito.any());
	}

	@Test
	@DisplayName("Deve remover um produto")
	public void shouldDeleteProduct() {
		Long productId = 1L;

		Product product = Product.builder().withId(productId).withName("Produto").withDescription("Descrição").build();

		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		service.delete(productId);

		Mockito.verify(productRepository, Mockito.times(1)).delete(product);
		Mockito.verify(productItemRepository, Mockito.times(1)).deleteAllByProduct(product);
	}

	@Test
	@DisplayName("Deve lançar a exceção ProductNotFoundException caso tente remover um produto que não existe")
	public void shouldNotDeleteProductWhenProductDoesNotExists() {
		Long productId = 1L;

		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class, () -> service.delete(productId));

		Mockito.verify(productRepository, Mockito.times(0)).delete(Mockito.any());
		Mockito.verify(productItemRepository, Mockito.times(0)).deleteAllByProduct(Mockito.any());
	}

}

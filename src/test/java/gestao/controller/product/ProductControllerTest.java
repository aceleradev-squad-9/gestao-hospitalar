package gestao.controller.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import gestao.exception.product.ProductNotFoundException;
import gestao.model.product.Product;
import gestao.service.product.ProductService;

@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProductService productService;

	@InjectMocks
	@Autowired
	private ProductController hospitalController;

	@Test
	@DisplayName("Deve receber o body em formato json com as informações do produto criado e também deve receber http status code 201.")
	public void shouldReceiveStatusCreatedAndBodyWithProduct() throws Exception {

		Product product = Product.builder().withName("Produto A").withDescription("Descrição").build();

		Mockito.when(productService.create(product)).thenReturn(product);

		ObjectMapper objectMapper = new ObjectMapper();
		String productJson = objectMapper.writeValueAsString(product);

		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.post("/product").contentType(MediaType.APPLICATION_JSON)
						.content(productJson).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn();

		assertEquals(productJson, mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber o http status 400 e os erros de validação em formato json ao criar um produto com dados inválidos")
	public void shouldReceiveBadRequestStatusAndBodyWithValidationErrorsInProductCreation() throws Exception {

		Product product = Product.builder().withName("").withDescription("").build();

		Mockito.when(productService.create(product)).thenReturn(product);

		ObjectMapper objectMapper = new ObjectMapper();
		String productJson = objectMapper.writeValueAsString(product);

		mvc.perform(MockMvcRequestBuilders.post("/product").contentType(MediaType.APPLICATION_JSON).content(productJson)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.name[0]", Matchers.is("O produto deve possuir um nome.")))
				.andExpect(jsonPath("$.errors.description[0]", Matchers.is("O produto deve possuir uma descrição.")))
				.andReturn();

		Mockito.verify(productService, Mockito.times(0)).create(Mockito.any());
	}

	@Test
	@DisplayName("Deve receber o http status 200 e uma lista de produtos em formato json")
	public void shouldReceiveAllProducts() throws Exception {

		List<Product> productList = Arrays.asList(
			Product.builder().withName("Produto A").withDescription("Descrição").build(),
			Product.builder().withName("Produto B").withDescription("Descrição").build()
		);

		Page<Product> productListPage = new PageImpl<>(productList);
		Mockito.when(
			productService.find(isA(PageRequest.class))
		).thenReturn(productListPage);

		ObjectMapper objectMapper = new ObjectMapper();
		String productListJson = objectMapper.writeValueAsString(productListPage);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
			.get("/product?page=0&size=" + productList.size())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(productListJson, mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber o http status 200 e um produto em formato json, obtido a partir do seu identificador único.")
	public void shouldReceiveProduct() throws Exception {

		Long productId = 1L;

		Product product = Product.builder().withName("Produto A").withDescription("Descrição").build();

		Mockito.when(productService.findById(productId)).thenReturn(product);

		ObjectMapper objectMapper = new ObjectMapper();
		String productJson = objectMapper.writeValueAsString(product);

		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.get(String.format("/product/%s", productId))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertEquals(productJson, mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber o http status 404 e lançar a exceção ProductNotFoundException ao buscar um produto não cadastrado.")
	public void shouldThrowProductNotFoundExceptionInProductSearch() throws Exception {

		Long productId = 1L;

		Mockito.when(productService.findById(productId)).thenThrow(new ProductNotFoundException());

		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.get(String.format("/product/%s", productId))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	@DisplayName("Deve receber o http status 200 e receber o produto com os dados atualizados.")
	public void shouldReceiveUpdatedProduct() throws Exception {

		Long productId = 1L;

		Product product = Product.builder().withName("Produto A").withDescription("Descrição").build();
		Product updatedProduct = Product.builder().withId(productId).withName("Produto A").withDescription("Descrição").build();

		Mockito.when(productService.update(productId, product)).thenReturn(updatedProduct);

		ObjectMapper objectMapper = new ObjectMapper();
		
		String productJson = objectMapper.writeValueAsString(product);
		String updatedProductJson = objectMapper.writeValueAsString(updatedProduct);
		
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(String.format("/product/%s", productId))
						.content(productJson)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertEquals(updatedProductJson, mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	@DisplayName("Deve receber o http status 404 e lançar a exceção ProductNotFoundException ao tentar atualizar um produto não cadastrado")
	public void shouldThrowProductNotFoundExceptionInProductUpdate() throws Exception {

		Long productId = 1L;

		Product product = Product.builder().withName("Produto A").withDescription("Descrição").build();

		Mockito.when(productService.update(productId, product)).thenThrow(new ProductNotFoundException());

		ObjectMapper objectMapper = new ObjectMapper();
		String productJson = objectMapper.writeValueAsString(product);

		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(String.format("/product/%s", productId))
						.content(productJson)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	@DisplayName("Deve receber o http status 400 e os erros de validação em formato json ao tentar atualizar um produto com dados inválidos")
	public void shouldReceiveBadRequestStatusAndBodyWithValidationErrorsInProductUpdate() throws Exception {

		Long productId = 1L;

		Product product = Product.builder().withName(null).withDescription(null).build();

		ObjectMapper objectMapper = new ObjectMapper();
		String productJson = objectMapper.writeValueAsString(product);

		mvc.perform(MockMvcRequestBuilders.put(String.format("/product/%s", productId))
						.content(productJson)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.name[0]", Matchers.is("O produto deve possuir um nome.")))
				.andExpect(jsonPath("$.errors.description[0]", Matchers.is("O produto deve possuir uma descrição.")))
				.andReturn();
		
		Mockito.verify(productService, Mockito.times(0)).update(Mockito.anyLong(), Mockito.any());
	}

	@Test
	@DisplayName("Deve receber o http status 204 informando que o produto foi removido com sucesso")
	public void shouldDeleteProduct() throws Exception {

		Long productId = 1L;

		mvc.perform(MockMvcRequestBuilders.delete(String.format("/product/%s", productId))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent()).andReturn();
		
		Mockito.verify(productService, Mockito.times(1)).delete(productId);
	}
	
	@Test
	@DisplayName(" Deve receber o http status 404 e lançar a exceção ProductNotFoundException ao tentar remover um produto não cadastrado")
	public void shouldThrowProductNotFoundExceptionInProductDelete() throws Exception {

		Long productId = 1L;

		Mockito.doThrow(new ProductNotFoundException()).when(productService).delete(productId);
		
		mvc.perform(MockMvcRequestBuilders.delete(String.format("/product/%s", productId))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
	}
	
}

package gestao.controller.hospital;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.exception.product.ProductNotFoundException;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.model.product.dto.ProductAmountDto;
import gestao.model.product.dto.ProductItemDto;
import gestao.service.hospital.HospitalService;
import gestao.service.hospital.HospitalStockService;
import gestao.service.product.ProductService;

@WebMvcTest(controllers = HospitalStockController.class)
public class HospitalStockControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private HospitalStockService hospitalStockService;

	@MockBean
	private HospitalService hospitalService;

	@MockBean
	private ProductService productService;

	@InjectMocks
	@Autowired
	private HospitalStockController hospitalController;

	@Test
	@DisplayName("Deve receber o body em formato json com as informações do item de produto criado e também deve receber http status code 201.")
	public void shouldReceiveStatusCreatedAndBodyWithProductItemWhenAddProductInStock() throws Exception {
		Long hospitalId = 1L;
		Long productId = 1L;

		Hospital hospital = Hospital
				.createFromDto(new HospitalDto("Hospital A", "Descrição.", 3, Mockito.mock(Address.class)));
		Product product = Product.builder().withName("Produto A").withDescription("Descrição").build();
		ProductItem productItem = ProductItem.builder().withAmount(10).withProduct(product).withHospital(hospital)
				.withExpirationDate(LocalDate.now().plusYears(1)).build();

		ProductItemDto productItemDto = productItem.convertToDto();

		Mockito.when(hospitalStockService.addProductInStock(Mockito.anyLong(), Mockito.any(Product.class),
				Mockito.any(ProductItemDto.class))).thenReturn(productItem);

		Mockito.when(productService.findById(productId)).thenReturn(product);

		final ObjectMapper objectMapper = new ObjectMapper();
		final String productItemDtoJson = objectMapper.writeValueAsString(productItemDto);

		MvcResult mvcResult = mvc.perform(put(String.format("/hospital/%s/stock/%s", hospitalId, productId))
				.contentType(MediaType.APPLICATION_JSON).content(productItemDtoJson).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertEquals(productItemDtoJson, mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber http status 404 ao tentar adicionar o produto no estoque de um hospital não cadastrado.")
	public void shouldReceiveHospitalNotFoundWhenAddProductInStock() throws Exception {

		ProductItemDto dto = new ProductItemDto();
		dto.setAmount(10);
		dto.setExpirationDate(LocalDate.now().plusYears(1L));

		Mockito.when(hospitalStockService.addProductInStock(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenThrow(new HospitalNotFoundException());

		ObjectMapper objectMapper = new ObjectMapper();
		String productItemDtoJson = objectMapper.writeValueAsString(dto);

		MvcResult mvcResult = mvc
				.perform(put(String.format("/hospital/%s/stock/%s", 1L, 1L)).contentType(MediaType.APPLICATION_JSON)
						.content(productItemDtoJson).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber http status 404 ao tentar adicionar um produto não cadastrado no estoque do hospital.")
	public void shouldReceiveProductNotFoundWhenAddProductInStock() throws Exception {

		ProductItemDto dto = new ProductItemDto();
		dto.setAmount(10);
		dto.setExpirationDate(LocalDate.now().plusYears(1L));

		Mockito.when(productService.findById(Mockito.any())).thenThrow(new ProductNotFoundException());

		ObjectMapper objectMapper = new ObjectMapper();
		String productItemDtoJson = objectMapper.writeValueAsString(dto);

		mvc.perform(put(String.format("/hospital/%s/stock/%s", 1L, 1L)).contentType(MediaType.APPLICATION_JSON)
				.content(productItemDtoJson).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andReturn();

		Mockito.verify(hospitalStockService, Mockito.times(0)).addProductInStock(Mockito.any(), Mockito.any(),
				Mockito.any());
	}

	@Test
	@DisplayName("Deve receber http status 400 ao tentar adicionar um produto no estoque sem informar os dados do item do produto.")
	public void shouldReceiveValidationErrorsWhenAddProductInStockWithoutItemInfo() throws Exception {

		ProductItemDto dto = new ProductItemDto();
		dto.setAmount(null);
		dto.setExpirationDate(null);

		ObjectMapper objectMapper = new ObjectMapper();
		String productItemDtoJson = objectMapper.writeValueAsString(dto);

		mvc.perform(put(String.format("/hospital/%s/stock/%s", 1L, 1L)).contentType(MediaType.APPLICATION_JSON)
				.content(productItemDtoJson).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.['amount'][0]", is("Informe a quantidade do produto")))
				.andExpect(jsonPath("$.errors.['expirationDate'][0]",
						is("A data de validade não foi informada ou é inválida.")))
				.andReturn();

		Mockito.verify(productService, Mockito.times(0)).findById(Mockito.anyLong());

		Mockito.verify(hospitalStockService, Mockito.times(0)).addProductInStock(Mockito.any(), Mockito.any(),
				Mockito.any());
	}

	@Test
	@DisplayName("Deve receber http status 400 ao tentar adicionar um produto no estoque com dados inválidos.")
	public void shouldReceiveValidationErrorsWhenAddProductInStockWithInvalidInfo() throws Exception {

		ProductItemDto dto = new ProductItemDto();
		dto.setAmount(-1);
		dto.setExpirationDate(LocalDate.now().minusDays(1));

		ObjectMapper objectMapper = new ObjectMapper();
		String productItemDtoJson = objectMapper.writeValueAsString(dto);

		mvc.perform(put(String.format("/hospital/%s/stock/%s", 1L, 1L)).contentType(MediaType.APPLICATION_JSON)
				.content(productItemDtoJson).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.['amount'][0]", is("A quantidade do produto deve ser maior que 0")))
				.andExpect(jsonPath("$.errors.['expirationDate'][0]", is("A data de validade deve estar no futuro.")))
				.andReturn();

		Mockito.verify(productService, Mockito.times(0)).findById(Mockito.anyLong());

		Mockito.verify(hospitalStockService, Mockito.times(0)).addProductInStock(Mockito.any(), Mockito.any(),
				Mockito.any());
	}

	@Test
	@DisplayName("Deve receber uma lista com o estoque de todos os produtos de um hospital")
	public void shouldReceiveAListWithAllStockProducts() throws Exception {

		Long hospitalId = 1L;
		Hospital hospital = Mockito.mock(Hospital.class);
		ProductItem productItemA = ProductItem.builder().withAmount(10).withHospital(hospital)
				.withProduct(Product.builder().withName("Product A").withDescription("Descrição").build()).build();

		ProductItem productItemB = ProductItem.builder().withAmount(10).withHospital(hospital)
				.withProduct(Product.builder().withName("Product B").withDescription("Descrição").build()).build();

		List<ProductItemDto> dtos = Arrays.asList(productItemA.convertToDto(), productItemB.convertToDto());

		Page<ProductItemDto> dtosPage = new PageImpl<>(dtos);

		when(hospitalStockService.findAllHospitalProductItems(isA(Long.class), isA(Pageable.class)))
				.thenReturn(dtosPage);
		MvcResult mvcResult = mvc
				.perform(get(String.format("/hospital/%s/stock?page=0&size=" + dtos.size(), hospitalId))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		ObjectMapper objectMapper = new ObjectMapper();

		assertEquals(objectMapper.writeValueAsString(dtosPage), mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber http status 404 ao tentar buscar os produtos de um hospital não cadastrado.")
	public void shouldReceiveHospitalNotFoundWhenFindStockProducts() throws Exception {
		Long NOT_EXISTING_ID = 1L;

		Mockito.doThrow(new HospitalNotFoundException()).when(hospitalService).verifyIfExistsById(NOT_EXISTING_ID);

		MvcResult mvcResult = mvc
				.perform(get(String.format("/hospital/%s/stock?page=0&size=1", NOT_EXISTING_ID))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber o estoque de um produto do hospital")
	public void shouldReceiveAProductFromStockProducts() throws Exception {

		Long hospitalId = 1L;
		Long productId = 1L;

		Hospital hospital = Mockito.mock(Hospital.class);

		Product product = Product.builder().withName("Product A").withDescription("Descrição").build();

		ProductItem productItemA = ProductItem.builder().withId(1L).withAmount(10).withHospital(hospital)
				.withProduct(product).withExpirationDate(LocalDate.now().plusYears(1)).build();

		ProductItem productItemB = ProductItem.builder().withId(1L).withAmount(20).withHospital(hospital)
				.withProduct(product).withExpirationDate(LocalDate.now().plusYears(2)).build();

		List<ProductItem> productItemList = Arrays.asList(productItemA, productItemB);
		Page<ProductItem> productItemListPage = new PageImpl<>(productItemList);

		Mockito.when(productService.findById(productId)).thenReturn(product);
		Mockito.when(
				hospitalStockService.findProductInStock(Mockito.anyLong(), isA(Product.class), isA(Pageable.class)))
				.thenReturn(productItemListPage);

		MvcResult mvcResult = mvc.perform(get(
				String.format("/hospital/%s/stock/%s?page=0&size=%s", hospitalId, productId, productItemList.size()))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		ObjectMapper objectMapper = new ObjectMapper();

		assertEquals(objectMapper.writeValueAsString(productItemListPage.map(ProductItem::convertToDto)),
				mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber http status 404 ao tentar buscar um produto de um hospital não cadastrado.")
	public void shouldReceiveHospitalNotFoundWhenFindProductFromStock() throws Exception {

		Long hospitalId = 1L;
		Long productId = 1L;
		Integer listSize = 10;
		Product product = Product.builder().withName("Product A").withDescription("Descrição").build();

		Mockito.when(productService.findById(productId)).thenReturn(product);
		Mockito.when(
				hospitalStockService.findProductInStock(Mockito.anyLong(), isA(Product.class), isA(Pageable.class)))
				.thenThrow(new HospitalNotFoundException());

		MvcResult mvcResult = mvc
				.perform(get(String.format("/hospital/%s/stock/%s?page=0&size=%s", hospitalId, productId, listSize))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber http status 404 ao tentar buscar um produto não cadastrado.")
	public void shouldReceiveProductNotFoundWhenFindProductFromStock() throws Exception {

		Mockito.when(productService.findById(1L)).thenThrow(new ProductNotFoundException());

		MvcResult mvcResult = mvc
				.perform(get(String.format("/hospital/%s/stock/%s?page=0&size=%s", 1L, 1L, 10))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		Mockito.verify(hospitalStockService, Mockito.times(0)).findProductInStock(Mockito.any(), Mockito.any(),
				Mockito.any(Pageable.class));

		assertEquals("", mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber um produto do estoque quando solicitado de outro hospital.")
	public void shouldReceiveProductWhenOrderProductFromHospital() throws Exception {
		Long hospitalId = 1L;
		Long productId = 1L;

		Hospital hospital = Mockito.mock(Hospital.class);

		Product product = Product.builder().withName("Product A").withDescription("Descrição").build();

		ProductAmountDto productAmountDto = new ProductAmountDto(10);
		
		ProductItem productItem = ProductItem.builder()
				.withExpirationDate(LocalDate.now().plusYears(1))
				.withAmount(productAmountDto.getAmount())
				.withHospital(hospital).withProduct(product).build();

		List<Hospital> nearestHospitals = Arrays.asList(Mockito.mock(Hospital.class));
		Mockito.when(hospitalService.findById(hospitalId)).thenReturn(hospital);

		Mockito.when(hospitalService.findNearestHospitals(hospital)).thenReturn(nearestHospitals);

		Mockito.when(productService.findById(productId)).thenReturn(product);

		Mockito.when(hospitalStockService.transferProductItemFromTheFirstAbleHospital(nearestHospitals, hospital,
				product, productItem.getAmount())).thenReturn(productItem);

		ObjectMapper objectMapper = new ObjectMapper();
		String productAmountDtoJson = objectMapper.writeValueAsString(productAmountDto);

		MvcResult mvcResult = mvc.perform(put(String.format("/hospital/%s/order/%s", hospitalId, productId))
				.contentType(MediaType.APPLICATION_JSON).content(productAmountDtoJson).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertEquals(objectMapper.writeValueAsString(productItem.convertToDto()),
				mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("Deve receber http status 404 ao solicitar para outro hospital, um produto não cadastrado.")
	public void shouldReceiveProductNotFoundWhenOrderProductFromHospital() throws Exception {
		Long hospitalId = 1L;
		Long productId = 1L;

		ProductAmountDto productAmountDto = new ProductAmountDto(10);

		Mockito.when(productService.findById(productId)).thenThrow(new ProductNotFoundException());

		ObjectMapper objectMapper = new ObjectMapper();

		String productAmountDtoJson = objectMapper.writeValueAsString(productAmountDto);

		MvcResult mvcResult = mvc.perform(put(String.format("/hospital/%s/order/%s", hospitalId, productId))
				.contentType(MediaType.APPLICATION_JSON).content(productAmountDtoJson).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		assertEquals("", mvcResult.getResponse().getContentAsString());

		Mockito.verify(hospitalStockService, Mockito.times(0)).transferProductItemFromTheFirstAbleHospital(
				Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyInt());
	}

	@Test
	@DisplayName("Deve receber http status 400 ao solicitar para outro hospital uma quantidade inválida de um produto.")
	public void shouldReceiveValidationErrorsWhenOrderProductFromHospitalWithInvalidAmount() throws Exception {
		Long hospitalId = 1L;
		Long productId = 1L;

		ProductAmountDto productAmountDto = new ProductAmountDto(-1);

		ObjectMapper objectMapper = new ObjectMapper();

		String productAmountDtoJson = objectMapper.writeValueAsString(productAmountDto);

		mvc.perform(put(String.format("/hospital/%s/order/%s", hospitalId, productId))
				.contentType(MediaType.APPLICATION_JSON).content(productAmountDtoJson).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.['amount'][0]", is("A quantidade do produto deve ser maior que 0")))
				.andReturn();

		Mockito.verify(productService, Mockito.times(0)).findById(Mockito.anyLong());
		Mockito.verify(hospitalStockService, Mockito.times(0)).transferProductItemFromTheFirstAbleHospital(
				Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyInt());
	}

}

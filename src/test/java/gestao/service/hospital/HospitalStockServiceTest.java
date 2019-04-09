package gestao.service.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.exception.hospital.NoHospitalAbleToTransferProductException;
import gestao.helper.hospital.HospitalHelper;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.service.product.ProductItemService;

@SpringBootTest
public class HospitalStockServiceTest {
	@MockBean
	private HospitalService mockedHospitalService;

	@MockBean
	private ProductItemService mockedProductItemService;

	@InjectMocks
	@Autowired
	private HospitalStockService hospitalStockService;

	@Test
	@DisplayName("Deve adicionar um produto no estoque do hospital.")
	public void shouldAddStockProduct() {
		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;

		Hospital hospital = Hospital
				.createFromDto(new HospitalDto("Hospital", "Descrição", 123, Mockito.mock(Address.class)));

		Product product = Product.builder().withName("Produto").withDescription("Descrição").build();

		ProductItem expectedProductItem = ProductItem.builder().withAmount(PRODUCT_AMOUNT)
				.withExpirationDate(LocalDate.now().plusYears(2)).withHospital(hospital).withProduct(product).build();

		when(mockedProductItemService.findProductItem(hospital, product, expectedProductItem.getExpirationDate()))
				.thenReturn(Optional.empty());

		when(mockedHospitalService.findById(HOSPITAL_ID)).thenReturn(hospital);

		when(mockedHospitalService.save(hospital)).thenReturn(hospital);

		ProductItem obtainedProductItem = hospitalStockService.addProductInStock(HOSPITAL_ID, product,
				expectedProductItem.convertToDto());

		assertEquals(expectedProductItem.getProductName(), obtainedProductItem.getProductName());
		assertEquals(expectedProductItem.getProductDescription(), obtainedProductItem.getProductDescription());
		assertEquals(expectedProductItem.getAmount(), obtainedProductItem.getAmount());
		assertEquals(expectedProductItem.getExpirationDate(), obtainedProductItem.getExpirationDate());
	}

	@Test
	@DisplayName("Deve atualizar a quantidade de um produto no estoque do hospital.")
	public void shouldUpdateStockProduct() {
		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;

		Hospital hospital = Hospital
				.createFromDto(new HospitalDto("Hospital", "Descrição", 123, Mockito.mock(Address.class)));

		Product product = Product.builder().withName("Produto").withDescription("Descrição").build();

		ProductItem expectedProductItem = ProductItem.builder().withAmount(PRODUCT_AMOUNT)
				.withExpirationDate(LocalDate.now().plusYears(2)).withHospital(hospital).withProduct(product).build();

		hospital.addProductInStock(product, expectedProductItem.getAmount(), expectedProductItem.getExpirationDate());

		when(mockedProductItemService.findProductItem(hospital, product, expectedProductItem.getExpirationDate()))
				.thenReturn(Optional.of(expectedProductItem));

		when(mockedHospitalService.findById(HOSPITAL_ID)).thenReturn(hospital);

		when(mockedHospitalService.save(hospital)).thenReturn(hospital);

		ProductItem obtainedProductItem = hospitalStockService.addProductInStock(HOSPITAL_ID, product,
				expectedProductItem.convertToDto());

		assertEquals(expectedProductItem.getProductName(), obtainedProductItem.getProductName());
		assertEquals(expectedProductItem.getProductDescription(), obtainedProductItem.getProductDescription());
		assertEquals(PRODUCT_AMOUNT * 2, obtainedProductItem.getAmount().intValue());
		assertEquals(expectedProductItem.getExpirationDate(), obtainedProductItem.getExpirationDate());
	}

	@Test
	@DisplayName("Deve lançar a exceção HospitalNotFoundException ao tentar adicionar um produto em um hospital não existente.")
	public void shouldThrowsHospitalNotFoundWhenAddStockProduct() {
		
		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;
		final LocalDate EXPIRATION_DATE = LocalDate.now().plusYears(1);
		
		Product product = Product.builder().withName("Produto").withDescription("Descrição").build();
		
		ProductItem productItem = ProductItem.builder().withAmount(PRODUCT_AMOUNT)
				.withExpirationDate(EXPIRATION_DATE)
				.withProduct(product).build();

		when(mockedHospitalService.findById(Mockito.anyLong())).thenThrow(new HospitalNotFoundException());

		assertThrows(HospitalNotFoundException.class,
				() -> hospitalStockService.addProductInStock(HOSPITAL_ID, product, productItem.convertToDto()));

		Mockito.verify(mockedProductItemService, Mockito.times(0)).findProductItem(Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.verify(mockedHospitalService, Mockito.times(0)).save(Mockito.any());
	}

	@Test
	@DisplayName("Deve buscar um produto do estoque do hospital.")
	public void shouldFindProductInStock() {
		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;

		Hospital hospital = Hospital
				.createFromDto(new HospitalDto("Hospital", "Descrição", 123, Mockito.mock(Address.class)));

		Product product = Product.builder().withId(1L).withName("Produto").withDescription("Descrição").build();

		hospital.addProductInStock(product, PRODUCT_AMOUNT, LocalDate.now().plusYears(2));
		hospital.addProductInStock(product, PRODUCT_AMOUNT, LocalDate.now().plusYears(1));

		List<ProductItem> stock = hospital.getStock();
		Page<ProductItem> stockPage = new PageImpl<>(stock);

		when(mockedHospitalService.findById(HOSPITAL_ID)).thenReturn(hospital);

		when(mockedProductItemService.findProductItems(hospital, product, Pageable.unpaged())).thenReturn(stockPage);

		Page<ProductItem> obtainedStockPage = hospitalStockService.findProductInStock(HOSPITAL_ID, product,
				Pageable.unpaged());

		obtainedStockPage.getContent().stream().forEach((productItem) -> {
			assertEquals(product.getName(), productItem.getProductName());
			assertEquals(product.getDescription(), productItem.getProductDescription());
			assertEquals(PRODUCT_AMOUNT, productItem.getAmount());
		});

	}

	@Test
	@DisplayName("Deve lançar a exceção HospitalNotFoundException ao tentar buscar um produto de um hospital não existente.")
	public void shouldThrowsHospitalNotFoundWhenFindProductFromStock() {
		final Long HOSPITAL_ID = 1L;
		
		Product product = Product.builder().withId(1L).withName("Produto").withDescription("Descrição").build();

		when(mockedHospitalService.findById(HOSPITAL_ID)).thenThrow(new HospitalNotFoundException());

		assertThrows(HospitalNotFoundException.class,
				() -> hospitalStockService.findProductInStock(HOSPITAL_ID, product, Pageable.unpaged()));

		Mockito.verify(mockedProductItemService, Mockito.times(0)).findProductItems(Mockito.any(), Mockito.any(),
				Mockito.any());
	}

	@Test
	@DisplayName("Transferir itens de um produto de um hospital B para um hospital A, onde B é o hospital mais próximo de A e que pode transferir os itens.")
	public void shouldOrderProductFromNearestHospital() {

		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;
		final Integer REQUESTED_AMOUNT = 5;
		final LocalDate EXPIRATION_DATE = LocalDate.now().plusYears(1);

		Hospital hospitalA = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID);
		Hospital hospitalB = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 1);
		Hospital hospitalC = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 2);

		Product product = Product.builder().withName("Produto").withDescription("Descrição").build();

		ProductItem productItemA = hospitalA.addProductInStock(product, PRODUCT_AMOUNT, EXPIRATION_DATE);
		ProductItem productItemB = hospitalB.addProductInStock(product, PRODUCT_AMOUNT, EXPIRATION_DATE);

		List<Hospital> hospitals = Arrays.asList(hospitalB, hospitalC);

		when(mockedHospitalService.findNearestHospitals(hospitalA)).thenReturn(hospitals);

		when(mockedProductItemService.findProductItemAbleToTransferOnHospital(hospitalC, product, REQUESTED_AMOUNT,
				Hospital.MIN_STOCK_AMOUNT)).thenReturn(null);
		
		when(mockedProductItemService.findProductItemAbleToTransferOnHospital(hospitalB, product, REQUESTED_AMOUNT,
				Hospital.MIN_STOCK_AMOUNT)).thenReturn(productItemB);

		when(mockedProductItemService.findProductItem(hospitalA, product, productItemB.getExpirationDate()))
				.thenReturn(Optional.of(productItemA));

		when(mockedProductItemService.save(productItemA)).thenReturn(productItemA);

		ProductItem increasedProductItem = this.hospitalStockService
				.transferProductItemFromTheFirstAbleHospital(hospitals, hospitalA, product, REQUESTED_AMOUNT);

		assertEquals(product.getName(), increasedProductItem.getProductName());
		assertEquals(product.getDescription(), increasedProductItem.getProductDescription());
		assertEquals(PRODUCT_AMOUNT + REQUESTED_AMOUNT, increasedProductItem.getAmount().intValue());

		Mockito.verify(mockedProductItemService, Mockito.times(1)).reduceAmountOfItems(productItemB.getId(),
				REQUESTED_AMOUNT);
		Mockito.verify(mockedProductItemService, times(1)).save(productItemA);
	}

	@Test
	@DisplayName("Deve transferir produtos entre um hospital e o segundo mais próximo caso o primeiro não possua a quantidade mínima de produtos. ")
	public void shouldOrderProductFromNearestHospitalWithStockGreaterThanMinimum() {

		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;
		final Integer REQUESTED_AMOUNT = 5;
		final LocalDate EXPIRATION_DATE = LocalDate.now().plusYears(1);

		Hospital hospitalA = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID);
		Hospital hospitalB = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 1);
		Hospital hospitalC = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 2);

		Product product = Product.builder().withName("Produto").withDescription("Descrição").build();

		ProductItem productItemC = hospitalC.addProductInStock(product, PRODUCT_AMOUNT, EXPIRATION_DATE.plusYears(1L));

		List<Hospital> hospitals = Arrays.asList(hospitalB, hospitalC);

		when(mockedHospitalService.findNearestHospitals(hospitalA)).thenReturn(hospitals);

		when(mockedProductItemService.findProductItemAbleToTransferOnHospital(hospitalB, product, REQUESTED_AMOUNT,
				Hospital.MIN_STOCK_AMOUNT)).thenReturn(null);

		when(mockedProductItemService.findProductItemAbleToTransferOnHospital(hospitalC, product, REQUESTED_AMOUNT,
				Hospital.MIN_STOCK_AMOUNT)).thenReturn(productItemC);

		when(mockedProductItemService.findProductItem(hospitalA, product, productItemC.getExpirationDate()))
				.thenReturn(Optional.empty());

		ProductItem increasedProductItem = this.hospitalStockService
				.transferProductItemFromTheFirstAbleHospital(hospitals, hospitalA, product, REQUESTED_AMOUNT);

		assertEquals(product.getName(), increasedProductItem.getProductName());
		assertEquals(product.getDescription(), increasedProductItem.getProductDescription());
		assertEquals(REQUESTED_AMOUNT, increasedProductItem.getAmount());

		Mockito.verify(mockedProductItemService, Mockito.times(1)).reduceAmountOfItems(productItemC.getId(),
				REQUESTED_AMOUNT);

		Mockito.verify(mockedProductItemService, times(1)).save(increasedProductItem);
	}

	@Test
	@DisplayName("Deve lançar a exceção NearestHospitalNotFoundException quando não existirem hospitais próximos com estoque disponível para transferẽncia.")
	public void shouldThrowsNearestHospitalNotFound() {
		final Long HOSPITAL_ID = 1L;
		final Integer REQUESTED_AMOUNT = 5;

		Hospital hospitalA = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID);
		Hospital hospitalB = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 1);
		Hospital hospitalC = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 2);

		Product product = Product.builder().withName("Produto").withDescription("Descrição").build();

		List<Hospital> hospitals = Arrays.asList(hospitalB, hospitalC);

		when(mockedHospitalService.findNearestHospitals(hospitalA)).thenReturn(hospitals);

		when(mockedProductItemService.findProductItemAbleToTransferOnHospital(hospitalB, product, REQUESTED_AMOUNT,
				Hospital.MIN_STOCK_AMOUNT)).thenReturn(null);

		when(mockedProductItemService.findProductItemAbleToTransferOnHospital(hospitalC, product, REQUESTED_AMOUNT,
				Hospital.MIN_STOCK_AMOUNT)).thenReturn(null);

		assertThrows(NoHospitalAbleToTransferProductException.class, () -> {
			this.hospitalStockService.transferProductItemFromTheFirstAbleHospital(hospitals, hospitalA, product,
					REQUESTED_AMOUNT);
		});

		Mockito.verify(mockedProductItemService, Mockito.times(0)).reduceAmountOfItems(Mockito.anyLong(),
				Mockito.any());
		Mockito.verify(mockedProductItemService, times(0)).findProductItem(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.verify(mockedProductItemService, times(0)).save(Mockito.any());
		Mockito.verify(mockedHospitalService, times(0)).save(Mockito.any());
	}
}

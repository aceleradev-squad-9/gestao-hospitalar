package gestao.service.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
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

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.exception.hospital.NoHospitalAbleToTransferProductException;
import gestao.exception.hospital.ProductNotFoundInHospitalStockException;
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
		
		Hospital hospital = Hospital.createFromDto(
				new HospitalDto("Hospital", "Descrição", 123,
				Mockito.mock(Address.class)));

		Product product = Product.builder().withName("Produto")
				.withDescription("Descrição").build();

		when(mockedHospitalService.findById(HOSPITAL_ID))
				.thenReturn(hospital);

		when(mockedHospitalService.save(hospital))
				.thenReturn(hospital);

		ProductItem productItem = hospitalStockService.addProductInStock(HOSPITAL_ID, product, 10);
				
		assertEquals(product.getName(), productItem.getProductName());
		assertEquals(product.getDescription(), productItem.getProductDescription());
		assertEquals(PRODUCT_AMOUNT, productItem.getAmount());
	}
	
	@Test
	@DisplayName("Deve atualizar a quantidade de um produto no estoque do hospital.")
	public void shouldUpdateStockProduct() {
		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;
		
		Hospital hospital = Hospital.createFromDto(
				new HospitalDto("Hospital", "Descrição", 123,
				Mockito.mock(Address.class)));

		Product product = Product.builder().withName("Produto")
				.withDescription("Descrição").build();
		
		hospital.addProductInStock(product, PRODUCT_AMOUNT);

		when(mockedHospitalService.findById(HOSPITAL_ID))
				.thenReturn(hospital);

		when(mockedHospitalService.save(hospital))
				.thenReturn(hospital);

		ProductItem productItem = hospitalStockService.addProductInStock(HOSPITAL_ID, product, PRODUCT_AMOUNT);
				
		assertEquals(product.getName(), productItem.getProductName());
		assertEquals(product.getDescription(), productItem.getProductDescription());
		assertEquals(PRODUCT_AMOUNT * 2, productItem.getAmount().intValue());
	}
	
	@Test
	@DisplayName("Deve lançar a exceção HospitalNotFoundException ao tentar adicionar um produto em um hospital não existente.")
	public void shouldThrowsHospitalNotFoundWhenAddStockProduct() {
		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;

		when(mockedHospitalService.findById(HOSPITAL_ID)).thenThrow(new HospitalNotFoundException());

		assertThrows(
      HospitalNotFoundException.class,
      () -> hospitalStockService.addProductInStock(
        HOSPITAL_ID, 
        Mockito.any(), 
        PRODUCT_AMOUNT
      )
    );

		Mockito.verify(mockedHospitalService, Mockito.times(0)).save(Mockito.any());
	}
	
	@Test
	@DisplayName("Deve buscar um produto do estoque do hospital.")
	public void shouldFindProductInStock() {
		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;
		
		Hospital hospital = Hospital.createFromDto(
				new HospitalDto("Hospital", "Descrição", 123,
				Mockito.mock(Address.class)));

		Product product = Product.builder()
				.withId(1L)
				.withName("Produto")
				.withDescription("Descrição")
				.build();
		
		hospital.addProductInStock(product, PRODUCT_AMOUNT);

		when(mockedHospitalService.findById(HOSPITAL_ID))
				.thenReturn(hospital);

		ProductItem productItem = hospitalStockService.findProductInStock(HOSPITAL_ID, product);
		
		assertEquals(product.getName(), productItem.getProductName());
		assertEquals(product.getDescription(), productItem.getProductDescription());
		assertEquals(PRODUCT_AMOUNT, productItem.getAmount());
	}
	
	@Test
	@DisplayName("Deve lançar a exceção HospitalNotFoundException ao tentar buscar um produto de um hospital não existente.")
	public void shouldThrowsHospitalNotFoundWhenFindProductFromStock() {
		final Long HOSPITAL_ID = 1L;
		
		when(mockedHospitalService.findById(HOSPITAL_ID)).thenThrow(new HospitalNotFoundException());

		assertThrows(
      HospitalNotFoundException.class,
      () -> hospitalStockService.findProductInStock(HOSPITAL_ID, Mockito.any())
    );
	}
	
	@Test
	@DisplayName("Deve lançar a exceção ProductNotFoundInHospitalStockException ao tentar buscar um produto que não existe no estoque do hospital.")
	public void shouldThrowsProductNotFoundInHospitalStock() {
		
		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;
		
		Hospital hospital = Hospital.createFromDto(
				new HospitalDto("Hospital", "Descrição", 123,
				Mockito.mock(Address.class)));
				
		Product productA = Product.builder()
				.withId(1L)
				.withName("Produto A")
				.withDescription("Descrição").build();
		
		Product productB = Product.builder()
				.withId(2L)
				.withName("Produto B")
				.withDescription("Descrição").build();
		
		when(mockedHospitalService.findById(HOSPITAL_ID)).thenReturn(hospital);

		hospital.addProductInStock(productA, PRODUCT_AMOUNT);
		
		assertThrows(
      ProductNotFoundInHospitalStockException.class,
      () -> hospitalStockService.findProductInStock(HOSPITAL_ID, productB)
    );
	}
	
	@Test
	@DisplayName("Transferir itens de um produto de um hospital B para um hospital A, onde B é o hospital mais próximo de A e que pode transferir os itens.")
	public void shouldOrderProductFromNearestHospital() {

		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;
		final Integer REQUESTED_AMOUNT = 5;
		
		Hospital hospitalA = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID);
		Hospital hospitalB = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 1);
		Hospital hospitalC = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 2);

		Product product = Product.builder()
			.withName("Produto")
			.withDescription("Descrição")
			.build();
		
		hospitalA.addProductInStock(product, PRODUCT_AMOUNT);
		hospitalB.addProductInStock(product, PRODUCT_AMOUNT);
		hospitalC.addProductInStock(product, PRODUCT_AMOUNT);
		
		List<Hospital> hospitals = Arrays.asList(hospitalB, hospitalC);
		
		when(mockedHospitalService.findNearestHospitals(hospitalA))
				.thenReturn(hospitals);

		when(
			mockedProductItemService.checkIfHospitalIsAbleToTransferProductItems(
				hospitalB, 
				product, 
				REQUESTED_AMOUNT, 
				Hospital.MIN_STOCK_AMOUNT
			)
		).thenReturn(Boolean.TRUE);
		
		when(mockedHospitalService.save(hospitalA))
			.thenReturn(hospitalA);

		ProductItem increasedProductItem = this.hospitalStockService
      .transferProductItemFromTheFirstAbleHospital(
				hospitals,
        hospitalA, 
        product, 
        REQUESTED_AMOUNT
      );
		
		assertEquals(product.getName(), increasedProductItem.getProductName());
		assertEquals(product.getDescription(), increasedProductItem.getProductDescription());
		assertEquals(PRODUCT_AMOUNT + REQUESTED_AMOUNT, increasedProductItem.getAmount().intValue());
		
		Mockito.verify(mockedProductItemService, times(1))
			.checkIfHospitalIsAbleToTransferProductItems(
				hospitalB, 
				product, 
				REQUESTED_AMOUNT, 
				Hospital.MIN_STOCK_AMOUNT	
			);

		Mockito.verify(mockedProductItemService, times(1)).reduceAmountOfItems(
			hospitalB, 
			product, 
			REQUESTED_AMOUNT
		);

		Mockito.verify(mockedHospitalService, times(1)).save(hospitalA);
	}
	
	@Test
	@DisplayName("Deve transferir produtos entre um hospital e o segundo mais próximo caso o primeiro não possua a quantidade mínima de produtos. ")
	public void shouldOrderProductFromNearestHospitalWithStockGreaterThanMinimum() {
		
		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;
		final Integer MIN_PRODUCT_AMOUNT = 4;
		final Integer REQUESTED_AMOUNT = 5;
		
		Hospital hospitalA = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID);
		Hospital hospitalB = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 1);
		Hospital hospitalC = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 2);

		Product product = Product.builder()
			.withName("Produto")
			.withDescription("Descrição")
			.build();
		
		hospitalA.addProductInStock(product, PRODUCT_AMOUNT);
		hospitalB.addProductInStock(product, MIN_PRODUCT_AMOUNT);
		hospitalC.addProductInStock(product, PRODUCT_AMOUNT);
		
		List<Hospital> hospitals = Arrays.asList(hospitalB, hospitalC);
		
		when(mockedHospitalService.findNearestHospitals(hospitalA))
				.thenReturn(hospitals);

		when(
			mockedProductItemService.checkIfHospitalIsAbleToTransferProductItems(
				hospitalB, 
				product, 
				REQUESTED_AMOUNT, 
				Hospital.MIN_STOCK_AMOUNT
			)
		).thenReturn(Boolean.FALSE);

		when(
			mockedProductItemService.checkIfHospitalIsAbleToTransferProductItems(
				hospitalC, 
				product, 
				REQUESTED_AMOUNT, 
				Hospital.MIN_STOCK_AMOUNT
			)
		).thenReturn(Boolean.TRUE);
		
		when(mockedHospitalService.save(hospitalA))
			.thenReturn(hospitalA);

		ProductItem increasedProductItem = this.hospitalStockService
      .transferProductItemFromTheFirstAbleHospital(
				hospitals,
        hospitalA, 
        product, 
        REQUESTED_AMOUNT
      );
		
		assertEquals(product.getName(), increasedProductItem.getProductName());
		assertEquals(product.getDescription(), increasedProductItem.getProductDescription());
		assertEquals(PRODUCT_AMOUNT + REQUESTED_AMOUNT, increasedProductItem.getAmount().intValue());
		
		Mockito.verify(mockedProductItemService, times(1))
			.checkIfHospitalIsAbleToTransferProductItems(
				hospitalB, 
				product, 
				REQUESTED_AMOUNT, 
				Hospital.MIN_STOCK_AMOUNT	
			);

		Mockito.verify(mockedProductItemService, times(1))
			.checkIfHospitalIsAbleToTransferProductItems(
				hospitalC, 
				product, 
				REQUESTED_AMOUNT, 
				Hospital.MIN_STOCK_AMOUNT	
			);

		Mockito.verify(mockedProductItemService, times(1)).reduceAmountOfItems(
			hospitalC, 
			product, 
			REQUESTED_AMOUNT
		);

		Mockito.verify(mockedHospitalService, times(1)).save(hospitalA);
	}
	
	@Test
	@DisplayName("Deve lançar a exceção NearestHospitalNotFoundException quando não existirem hospitais próximos com estoque disponível para transferẽncia.")
	public void shouldThrowsNearestHospitalNotFound() {
		final Long HOSPITAL_ID = 1L;
		final Integer PRODUCT_AMOUNT = 10;
		final Integer MIN_PRODUCT_AMOUNT = 4;
		final Integer REQUESTED_AMOUNT = 5;
		
		Hospital hospitalA = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID);
		Hospital hospitalB = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 1);
		Hospital hospitalC = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL_ID + 2);

		Product product = Product.builder()
			.withName("Produto")
			.withDescription("Descrição")
			.build();
		
		hospitalA.addProductInStock(product, PRODUCT_AMOUNT);
		hospitalB.addProductInStock(product, MIN_PRODUCT_AMOUNT);
		hospitalC.addProductInStock(product, MIN_PRODUCT_AMOUNT);
		
		List<Hospital> hospitals = Arrays.asList(hospitalB, hospitalC);
		
		when(mockedHospitalService.findNearestHospitals(hospitalA))
				.thenReturn(hospitals);

		when(
			mockedProductItemService.checkIfHospitalIsAbleToTransferProductItems(
				hospitalB, 
				product, 
				REQUESTED_AMOUNT, 
				Hospital.MIN_STOCK_AMOUNT
			)
		).thenReturn(Boolean.FALSE);

		when(
			mockedProductItemService.checkIfHospitalIsAbleToTransferProductItems(
				hospitalC, 
				product, 
				REQUESTED_AMOUNT, 
				Hospital.MIN_STOCK_AMOUNT
			)
		).thenReturn(Boolean.FALSE);

		assertThrows(
			NoHospitalAbleToTransferProductException.class,
			() -> {
				this.hospitalStockService
					.transferProductItemFromTheFirstAbleHospital(
						hospitals,
						hospitalA, 
						product, 
						REQUESTED_AMOUNT
					);
			}
		);
	}
}
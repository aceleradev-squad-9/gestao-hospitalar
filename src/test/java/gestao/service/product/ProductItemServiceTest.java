package gestao.service.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import gestao.helper.hospital.HospitalHelper;
import gestao.model.hospital.Hospital;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.repository.product.ProductItemRepository;

@SpringBootTest
public class ProductItemServiceTest {

  @MockBean
  private ProductItemRepository productItemRepository;

  @InjectMocks
  @Autowired
  private ProductItemService productItemService;

  @Test
  @DisplayName("Testar se o método ProductItemService.findProductItemAbleToBeTransferedFromHospital está retornando o ProductItem correto.")
  public void findProductItemAbleToBeTransferedFromHospital(){
    final Long HOSPITAL1_ID = 1L;
    Hospital hospital1 = HospitalHelper.getAHospitalWithValidProperties(HOSPITAL1_ID);

    final Long PRODUCT1_ID = 1L;
    Product product1 = Product.builder()
      .withId(PRODUCT1_ID)
      .withName("product 1")
      .withDescription("description 1")
      .build();
 
    final Integer AMOUNT_TO_BE_TRANSFERRED = 5;

    ProductItem productItemToBeTransferred = ProductItem.builder()
      .withId(1L)
      .withAmount(10)
      .withHospital(hospital1)
      .withProduct(product1)
      .withExpirationDate(LocalDate.of(2020, 10, 10))
      .build();

    when(
      productItemRepository.checkIfAHospitalIsAbleToTransferItemsOfAProduct(
        hospital1.getId(),
        product1.getId(),
        AMOUNT_TO_BE_TRANSFERRED,
        Hospital.MIN_STOCK_AMOUNT
      )
    ).thenReturn(productItemToBeTransferred);

    ProductItem transferredProductItem = productItemService
      .findProductItemAbleToBeTransferedFromHospital(
        hospital1,
        product1,
        AMOUNT_TO_BE_TRANSFERRED,
        Hospital.MIN_STOCK_AMOUNT
      );

    assertEquals(productItemToBeTransferred, transferredProductItem);

    verify(productItemRepository, times(1)).checkIfAHospitalIsAbleToTransferItemsOfAProduct(
      hospital1.getId(),
      product1.getId(),
      AMOUNT_TO_BE_TRANSFERRED,
      Hospital.MIN_STOCK_AMOUNT
    );
  }
}
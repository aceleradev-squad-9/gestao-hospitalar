package gestao.model.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gestao.exception.hospital.InvalidAmountInStockException;
import gestao.helper.hospital.HospitalHelper;
import gestao.model.hospital.Hospital;

public class ProductItemTest {

  @Test
  @DisplayName("Testes unitários para o método ProductItem.reduceAmount")
  public void testReduceAmount(){
    Hospital hospital1 = HospitalHelper.getAHospitalWithValidProperties(1L);
    Product product1 = Product.builder()
      .withId(1L)
      .withName("produto 1")
      .withDescription("descrição do produto 1")
      .build();

    final Integer PRODUCT_INITIAL_AMOUNT = 10;
    ProductItem productItem = ProductItem.builder()
      .withId(1L)
      .withAmount(PRODUCT_INITIAL_AMOUNT)
      .withHospital(hospital1)
      .withProduct(product1)
      .withExpirationDate(LocalDate.of(2021, 10, 11))
      .build();

    assertThrows(
      InvalidAmountInStockException.class, 
      () -> productItem.reduceAmount(PRODUCT_INITIAL_AMOUNT+1)
    );

    final Integer PRODUCT_AMOUNT_TO_BE_REDUCED = 4;
    productItem.reduceAmount(PRODUCT_AMOUNT_TO_BE_REDUCED);
    assertEquals(
      PRODUCT_INITIAL_AMOUNT - PRODUCT_AMOUNT_TO_BE_REDUCED,
      productItem.getAmount().intValue()      
    );

    Integer productAmountThatShouldNotBeModified = productItem.getAmount();
    assertDoesNotThrow(() -> productItem.reduceAmount(null));
    assertEquals(
      productAmountThatShouldNotBeModified.intValue(),
      productItem.getAmount().intValue()
    );
  }

  @Test
  @DisplayName("Testes unitários para o método ProductItem.increaseAmount")
  public void testIncreaseAmount(){
    Hospital hospital1 = HospitalHelper.getAHospitalWithValidProperties(1L);
    Product product1 = Product.builder()
      .withId(1L)
      .withName("produto 1")
      .withDescription("descrição do produto 1")
      .build();

    final Integer PRODUCT_INITIAL_AMOUNT = 10;
    ProductItem productItem = ProductItem.builder()
      .withId(1L)
      .withAmount(PRODUCT_INITIAL_AMOUNT)
      .withHospital(hospital1)
      .withProduct(product1)
      .withExpirationDate(LocalDate.of(2021, 10, 11))
      .build();
    
    final Integer PRODUCT_AMOUNT_THAT_SHOULD_BE_ADDED = 2;
    productItem.increaseAmount(PRODUCT_AMOUNT_THAT_SHOULD_BE_ADDED);
    assertEquals(
      PRODUCT_INITIAL_AMOUNT + PRODUCT_AMOUNT_THAT_SHOULD_BE_ADDED,
      productItem.getAmount().intValue()
    );

    Integer amountThatShouldNotChange = productItem.getAmount();
    assertDoesNotThrow(() -> productItem.increaseAmount(null));
    assertEquals(
      amountThatShouldNotChange.intValue(), 
      productItem.getAmount().intValue()
    );
  }
}
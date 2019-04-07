package gestao.model.product;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

  @Test
  @DisplayName("O método Product.equals deve retornar true caso as duas referêcias apontem para o mesmo objeto ou se o id dos dois objetos forem iguais.")
  public void shouldReceiveTrueFromProductEqualsMethod(){
    Product product1 = Product.builder()
      .withId(1L)
      .build();

    Product product2 = product1;

    assertTrue(product1.equals(product2));

    Product product3 = Product.builder()
      .withId(1L)
      .build();

    assertTrue(product1.equals(product3));
  }

  @Test
  @DisplayName("O método Product.equals deve retornar false caso o objeto recebido como parâmetro seja null, não seja uma instância de Product ou os ids sejam diferentes.")
  public void shouldReceiveFalseFromProductEqualsMethod(){
    Product product1 = Product.builder().build();
    
    assertFalse(product1.equals(null));

    assertFalse(product1.equals(new String("not a product")));

    Product product2 = Product.builder().withId(1L).build();
    assertFalse(product1.equals(product2));

    Product product3 = Product.builder().withId(2L).build();
    assertFalse(product2.equals(product3));
  }
}
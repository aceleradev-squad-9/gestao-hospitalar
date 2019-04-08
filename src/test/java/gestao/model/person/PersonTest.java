package gestao.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PersonTest {

  @Test
  @DisplayName("O método Person.equals deve retornar true caso as duas referêcias apontem para o mesmo objeto ou se o id dos dois objetos forem iguais.")
  public void shouldReceiveTrueFromPersonEqualsMethod(){
    Person person1 = Person.builder()
      .withId(1L)
      .build();

    Person person2 = person1;

    assertTrue(person1.equals(person2));

    Person person3 = Person.builder()
      .withId(1L)
      .build();

    assertTrue(person1.equals(person3));
  }

  @Test
  @DisplayName("O método Person.equals deve retornar false caso o objeto recebido como parâmetro seja null, não seja uma instância de Person ou os ids sejam diferentes.")
  public void shouldReceiveFalseFromPersonEqualsMethod(){
    Person person1 = Person.builder().build();

    assertFalse(person1.equals(null));

    assertFalse(person1.equals(new String("not a person")));

    Person person2 = Person.builder().withId(1L).build();
    assertFalse(person1.equals(person2));

    Person person3 = Person.builder().withId(2L).build();
    assertFalse(person2.equals(person3));
  }
}
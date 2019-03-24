package gestao.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static gestao.util.IntegerUtil.fromStringToInteger;

class IntegerUtilTest {

  @Test
  @DisplayName("Deve converter a string '12345' para o inteiro 12345")
  public void shouldBeAbleToConvertToInteger(){
    final Integer num = fromStringToInteger("12345");
    assertEquals(Integer.valueOf(12345), num );
  }

  @Test
  @DisplayName("Deve converter a string '-12345' para o inteiro -12345")
  public void shouldBeAbleToConvertToInteger2(){
    final Integer num = fromStringToInteger("-12345");
    assertEquals(Integer.valueOf(-12345), num );
  }

  @Test
  @DisplayName("Não deve conseguir converter a string '12345.67'")
  public void shouldNotBeAbleToConvertToInteger(){
    final Integer num = fromStringToInteger("12345.67");
    assertEquals(null, num);
  }

  @Test
  @DisplayName("Não deve conseguir converter a string '12345a'")
  public void shouldNotBeAbleToConvertToInteger2(){
    final Integer num = fromStringToInteger("12345a");
    assertEquals(null, num);
  }

  @Test
  @DisplayName("Não deve conseguir converter a string ''")
  public void shouldNotBeAbleToConvertToInteger3(){
    final Integer num = fromStringToInteger("");
    assertEquals(null, num);
  }
}
package gestao.util;

import static gestao.util.DoubleUtil.fromStringToDouble;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DoubleUtilTest {

  @Test
  @DisplayName("Deve converter a string '12345' para o número 12345.0")
  public void shouldBeAbleToConvertToDouble(){
    Double num = fromStringToDouble("12345");
    assertEquals(Double.valueOf(12345), num);
  }

  @Test
  @DisplayName("Deve converter a string '12345.67' para o número 12345.67")
  public void shouldBeAbleToConvertToDouble2(){
    Double num = fromStringToDouble("12345.67");
    assertEquals(Double.valueOf(12345.67), num);
  }

  @Test
  @DisplayName("Deve converter a string '0.123' para o número 0.123")
  public void shouldBeAbleToConvertToDouble3(){
    Double num = fromStringToDouble("0.123");
    assertEquals(Double.valueOf(0.123), num);
  }

  @Test
  @DisplayName("Deve converter a string '+0.123' para o número +0.123")
  public void shouldBeAbleToConvertToDouble4(){
    Double num = fromStringToDouble("+0.123");
    assertEquals(Double.valueOf(+0.123), num);
  }

  @Test
  @DisplayName("Deve converter a string '-0.123' para o número -0.123")
  public void shouldBeAbleToConvertToDouble5(){
    Double num = fromStringToDouble("-0.123");
    assertEquals(Double.valueOf(-0.123), num);
  }

  @Test
  @DisplayName("Deve converter a string '-0.' para o número -0.")
  public void shouldBeAbleToConvertToDouble6(){
    Double num = fromStringToDouble("-0.");
    assertEquals(Double.valueOf(-0.), num);
  }

  @Test
  @DisplayName("Não deve converter a string '123a45'")
  public void shouldNotBeAbleToConvertToDouble(){
    Double num = fromStringToDouble("123a45");
    assertEquals(null, num);
  }

  @Test
  @DisplayName("Não deve converter a string '123.a'")
  public void shouldNotBeAbleToConvertToDouble2(){
    Double num = fromStringToDouble("123.a");
    assertEquals(null, num);
  }
}
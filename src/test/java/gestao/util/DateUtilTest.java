package gestao.util;

import static gestao.util.DateUtil.toDate;
import static gestao.util.DateUtil.toStr;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DateUtilTest {

  @Test
  @DisplayName("Deve receber LocalDate caso a String contenha uma data no formato: dd/MM/yyyy")
  public void shouldReceiveLocalDateIfTheStringIsWithTheCorrectFormat(){
    LocalDate localDate = toDate("01/02/1994");
    assertEquals(LocalDate.of(1994, 02, 01), localDate);
  }

  @Test
  @DisplayName("Deve converter a String '01/01/2001' para LocalDate")
  public void shouldConvertToLocalDate(){
    LocalDate localDate = toDate("01/01/2001");
    assertEquals(LocalDate.of(2001, 01, 01), localDate);
  }

  @Test
  @DisplayName("Deve converter a String '01/01/0001' para LocalDate")
  public void shouldConvertToLocalDate2(){
    LocalDate localDate = toDate("01/01/0001");
    assertEquals(LocalDate.of(0001, 01, 01), localDate);
  }

  @Test
  @DisplayName("Deve receber null caso a String não contenha uma data no formato: dd/MM/yyyy")
  public void shouldReceiveNullIfTheStringIsNotWithTheCorrectFormat(){
    LocalDate localDate = toDate("01/02/94");
    assertEquals(null, localDate);
  }

  @Test
  @DisplayName("Não deve converter a String '0001-01-01' para LocalDate")
  public void shouldNotConvertToLocalDate(){
    LocalDate localDate = toDate("0001-01-01");
    assertEquals(null, localDate);
  }

  @Test
  @DisplayName("Não deve converter a String '05/1/2001' para LocalDate")
  public void shouldNotConvertToLocalDate2(){
    LocalDate localDate = toDate("05/1/2001");
    assertEquals(null, localDate);
  }
}
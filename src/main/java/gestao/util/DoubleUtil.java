package gestao.util;

public class DoubleUtil {
  public static Double fromStringToDouble(String str) {
    Double num = null;
    try {
      num = Double.valueOf(str);
    } catch(NumberFormatException exception){
    }
    return num;
  }
}
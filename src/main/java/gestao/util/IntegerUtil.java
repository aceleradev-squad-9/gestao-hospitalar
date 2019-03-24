package gestao.util;

public class IntegerUtil {
  public static Integer fromStringToInteger(String str){
    Integer num = null;
    try {
      num = Integer.valueOf(str);
    } catch(NumberFormatException exception){
    }
    return num;
  }
}
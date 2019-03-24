package gestao.exception.handler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class BadRequestResponseEntity {

  private final LocalDateTime timestamp;
  
  private final Integer status = HttpStatus.BAD_REQUEST.value(); 

  private final String error = "Bad Request";

  private final Map<String,List<String>> errors;

  private final String message = "Informações inválidas.";

  public BadRequestResponseEntity(Map<String,List<String>> errors ){
    this.timestamp = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();
    this.errors = errors;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public Integer getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public Map<String, List<String>> getErrors() {
    return errors;
  }

  public String getMessage() {
    return message;
  }
}
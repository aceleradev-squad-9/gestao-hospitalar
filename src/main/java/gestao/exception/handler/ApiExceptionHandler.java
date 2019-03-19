package gestao.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers, 
      HttpStatus status,
      WebRequest request
    ) {
      BindingResult bindingResult = exception.getBindingResult();

      Map<String,String> validationMessages = new HashMap<>();
      bindingResult
        .getFieldErrors()
        .stream()
        .forEach(fieldError -> {
          validationMessages.put(
            fieldError.getField(), 
            fieldError.getDefaultMessage()
          );
        });

      return new ResponseEntity<>(validationMessages, HttpStatus.BAD_REQUEST);
    }
};
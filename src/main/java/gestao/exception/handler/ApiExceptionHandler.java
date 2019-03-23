package gestao.exception.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    Map<String,List<String>> validationMessages = new HashMap<>();
    bindingResult
      .getFieldErrors()
      .stream()
      .forEach(f -> {
        String field = f.getField();
  
        List<String> messagesForThisField = validationMessages.getOrDefault(
          field,
          new ArrayList<>()
        );
        
        messagesForThisField.add(f.getDefaultMessage());
        
        validationMessages.put(field, messagesForThisField);
      });
    
    BadRequestResponseEntity response = new BadRequestResponseEntity(
      validationMessages
    );
    

    return new ResponseEntity<>(response, headers, status);
  }
};
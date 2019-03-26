package gestao.exception.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    Map<String,List<String>> validationMessages = bindingResult
      .getFieldErrors()
      .stream()
      .collect(
        groupingBy(
          FieldError::getField,
          mapping(
            FieldError::getDefaultMessage,
            toList()
          )
        )
      );

    BadRequestResponseEntity response = new BadRequestResponseEntity(
      validationMessages
    );
    

    return new ResponseEntity<>(response, headers, status);
  }
};
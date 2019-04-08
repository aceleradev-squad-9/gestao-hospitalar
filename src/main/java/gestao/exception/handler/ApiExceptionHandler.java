package gestao.exception.handler;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		return new ResponseEntity<>(this.handleValidationErrors(exception.getBindingResult()), headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {

		return new ResponseEntity<>(this.handleValidationErrors(ex.getBindingResult()), headers, status);
	}

	private BadRequestResponseEntity handleValidationErrors(BindingResult bindingResult) {

		Map<String, List<String>> validationMessages = bindingResult.getFieldErrors().stream()
				.collect(groupingBy(FieldError::getField, mapping(FieldError::getDefaultMessage, toList())));

		return new BadRequestResponseEntity(validationMessages);
	}
}
package gestao.exception.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

public class ApiExceptionHandlerTest {

  private static final List<FieldError> fieldErrors = Arrays.asList(
    new FieldError("name1", "field1", "defaultMessage1"),
    new FieldError("name2", "field1", "defaultMessage2"),
    new FieldError("name2", "field1", "defaultMessage9"),

    new FieldError("name5", "field2", "defaultMessage3"),
    new FieldError("name6", "field2", "defaultMessage4"),

    new FieldError("name3", "field3", "defaultMessage5"),
    new FieldError("name4", "field3", "defaultMessage6"),

    new FieldError("name7", "field4", "defaultMessage7"),
    new FieldError("name8", "field4", "defaultMessage8")
  );

  private static final Map<String, List<String>> groupedFieldErrorMessages = new HashMap<>();

  static {
    groupedFieldErrorMessages.put(
      "field1",
      Arrays.asList("defaultMessage1", "defaultMessage2", "defaultMessage9")
    );
    groupedFieldErrorMessages.put(
      "field2",
      Arrays.asList("defaultMessage3", "defaultMessage4")
    );
    groupedFieldErrorMessages.put(
      "field3",
      Arrays.asList("defaultMessage5", "defaultMessage6")
    );
    groupedFieldErrorMessages.put(
      "field4",
      Arrays.asList("defaultMessage7", "defaultMessage8")
    );
  }

  private static MethodArgumentNotValidException mockedException;

  @Mock
  private static HttpHeaders httpHeaders;

  @Mock
  private static WebRequest request;

  private static ApiExceptionHandler apiExceptionHandler;

  @BeforeAll
  public static void setUp(){
    mockedException = mock(MethodArgumentNotValidException.class);

    BindingResult mockedBindingResult = mock(BindingResult.class);

    when(mockedBindingResult.getFieldErrors()).thenReturn(fieldErrors);
    when(mockedException.getBindingResult()).thenReturn(mockedBindingResult);

    apiExceptionHandler = new ApiExceptionHandler();
  }

  @Test
  @DisplayName("Deve receber uma reposta com o correto http status code.")
  public void shouldReceiveTheResponseWithTheCorrectStatus(){
    ResponseEntity<Object> responseEntity = apiExceptionHandler.handleMethodArgumentNotValid(
      mockedException, 
      httpHeaders, 
      HttpStatus.BAD_REQUEST, 
      request
    );

    assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("Deve receber uma reposta com o body correto contendo as mensagens de validação e detalhes sobre a resposta.")
  public void shouldReceiveTheResponseWithTheCorrectResponseBody(){

    ResponseEntity<Object> responseEntity = apiExceptionHandler.handleMethodArgumentNotValid(
      mockedException, 
      httpHeaders, 
      HttpStatus.BAD_REQUEST, 
      request
    );

    BadRequestResponseEntity badRequestResponse = (BadRequestResponseEntity) responseEntity.getBody();
    assertEquals("Bad Request", badRequestResponse.getError());
    assertEquals(Integer.valueOf(400), badRequestResponse.getStatus());
    assertEquals(groupedFieldErrorMessages, badRequestResponse.getErrors());
  }

} 

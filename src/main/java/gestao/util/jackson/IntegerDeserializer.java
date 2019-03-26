package gestao.util.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import static gestao.util.IntegerUtil.fromStringToInteger;

public class IntegerDeserializer extends StdDeserializer<Integer> {

  private static final long serialVersionUID = 5807996445382709202L;
  
  protected IntegerDeserializer(Class<?> t){
    super(t);
  }

  protected IntegerDeserializer(){
    this(null);
  }

	@Override
  public Integer deserialize(JsonParser jsonParser, DeserializationContext ctxt) 
    throws IOException, JsonProcessingException {
		return fromStringToInteger(jsonParser.readValueAs(String.class));
	}
  
}
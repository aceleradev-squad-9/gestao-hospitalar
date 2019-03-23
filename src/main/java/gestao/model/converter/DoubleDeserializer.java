package gestao.model.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import static gestao.util.DoubleUtil.fromStringToDouble;

public class DoubleDeserializer extends StdDeserializer<Double> {
  
  private static final long serialVersionUID = 1090905842177365755L;

  protected DoubleDeserializer(Class<?> t){
    super(t);
  }

  protected DoubleDeserializer(){
    this(null);
  }

	@Override
  public Double deserialize(JsonParser jsonParser, DeserializationContext ctxt) 
    throws IOException, JsonProcessingException {
		return fromStringToDouble(jsonParser.readValueAs(String.class));
	}
  
}
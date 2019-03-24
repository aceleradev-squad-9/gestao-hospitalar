package gestao.util.jackson;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import gestao.util.DateUtil;

/**
 * Classe responsável pela deserialização de uma {@link String}, de um JSON,
 * para um {@link LocalDate}.
 * 
 * @author edmilson.santana
 *
 */
public class DateDeserializer extends StdDeserializer<LocalDate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3177937977942194316L;

	protected DateDeserializer(Class<?> vc) {
		super(vc);
	}

	public DateDeserializer() {
		this(null);
	}

	@Override
	public LocalDate deserialize(JsonParser jsonParser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		return DateUtil.toDate(jsonParser.readValueAs(String.class));
	}

}

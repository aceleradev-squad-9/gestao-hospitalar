package gestao.util.jackson;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import gestao.util.DateUtil;

/**
 * Classe responsável pela serialização de um {@link LocalDateTime} em um formato de
 * {@link String} para um JSON.
 * 
 * @author edmilson.santana
 *
 */
public class DateTimeSerializer extends StdSerializer<LocalDateTime> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1292842189183809839L;

	protected DateTimeSerializer(Class<LocalDateTime> t) {
		super(t);
	}

	public DateTimeSerializer() {
		this(null);
	}

	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(DateUtil.toStr(value));
	}

}

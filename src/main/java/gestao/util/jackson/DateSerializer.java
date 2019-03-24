package gestao.util.jackson;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import gestao.util.DateUtil;

/**
 * Classe responsável pela serialização de um {@link LocalDate} em um formato de
 * {@link String} para um JSON.
 * 
 * @author edmilson.santana
 *
 */
public class DateSerializer extends StdSerializer<LocalDate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1292842189183809839L;

	protected DateSerializer(Class<LocalDate> t) {
		super(t);
	}

	public DateSerializer() {
		this(null);
	}

	@Override
	public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(DateUtil.toStr(value));
	}

}

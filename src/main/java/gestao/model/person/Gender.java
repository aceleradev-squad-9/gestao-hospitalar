package gestao.model.person;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * {@link Enum} responsável pela representação do sexo de uma pessoa.
 * 
 * @author edmilson.santana
 *
 */
public enum Gender implements Supplier<String> {
	MALE("Masculino"), FEMALE("Feminino"), OTHERS("Outros");

	private final String description;

	private static final Map<String, Gender> VALUES = Stream.of(Gender.values())
			.collect(Collectors.toMap((gender) -> gender.get().toLowerCase(), Function.identity()));

	Gender(String description) {
		this.description = description;
	}

	@JsonValue
	@Override
	public String get() {
		return description;
	}

	@JsonCreator
	public static Gender fromString(String value) {
		return VALUES.get(Optional.of(value).orElse("").toLowerCase());
	}

}

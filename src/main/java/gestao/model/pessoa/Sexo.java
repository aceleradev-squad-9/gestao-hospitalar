package gestao.model.pessoa;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * {@link Enum} responsável pela representação do sexo de uma {@link Pessoa}
 * 
 * @author edmilson.santana
 *
 */
public enum Sexo implements Supplier<String> {
	MASCULINO("Masculino"), FEMININO("Feminino"), OUTROS("Outros");

	private final String descricao;

	private static final Map<String, Sexo> VALUES = Stream.of(Sexo.values())
			.collect(Collectors.toMap((sexo) -> sexo.get().toLowerCase(), Function.identity()));

	Sexo(String descricao) {
		this.descricao = descricao;
	}

	@JsonValue
	@Override
	public String get() {
		return descricao;
	}

	@JsonCreator
	public static Sexo fromString(String value) {
		return VALUES.get(Optional.of(value).orElse("").toLowerCase());
	}

}

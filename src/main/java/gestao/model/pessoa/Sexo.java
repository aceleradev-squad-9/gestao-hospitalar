package gestao.model.pessoa;

import java.util.function.Supplier;

/**
 * {@link Enum} responsável pela representação do sexo de uma {@link Pessoa}
 * 
 * @author edmilson.santana
 *
 */
public enum Sexo implements Supplier<String> {
	MASCULINO("Masculino"), FEMININO("Feminino");


    private final String descricao;

    Sexo(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String get() {
        return descricao;
    }
}

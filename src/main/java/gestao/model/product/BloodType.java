package gestao.model.product;

import java.util.function.Supplier;

public enum BloodType implements Supplier<String> {
    A_POSITIVO("A+"),
    A_NEGATIVO("A-"),
    B_POSITIVO("B+"),
    B_NEGATIVO("B-"),
    AB_POSITIVO("AB+"),
    AB_NEGATIVO("AB-"),
    O_POSITIVO("O+"),
    O_NEGATIVO("O-");

    private String type;


    BloodType(String type) {
        this.type = type;
    }

    //@JsonValue
    //@Override
    public String get() {
        return type;
    }
}

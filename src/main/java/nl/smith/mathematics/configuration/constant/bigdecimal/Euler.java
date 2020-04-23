package nl.smith.mathematics.configuration.constant.bigdecimal;

import nl.smith.mathematics.configuration.constant.ConstantConfiguration;

import java.math.BigDecimal;

public class Euler extends ConstantConfiguration<BigDecimal> {

    private static final Euler instance = new Euler();

    private Euler() {
        super("nl.smith.mathematics.configuration.constant.Euler", BigDecimal.class);
    }

    public static BigDecimal get() {
        return instance.getValue();
    }

    public static void set(BigDecimal value) {
        instance.setValue(value);
    }

}

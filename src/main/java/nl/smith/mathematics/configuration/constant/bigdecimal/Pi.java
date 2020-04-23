package nl.smith.mathematics.configuration.constant.bigdecimal;

import nl.smith.mathematics.configuration.constant.ConstantConfiguration;

import java.math.BigDecimal;

public class Pi extends ConstantConfiguration<BigDecimal> {

    private static final Pi instance = new Pi();

    private Pi() {
        super("nl.smith.mathematics.configuration.constant.Pi", BigDecimal.class);
    }

    public static BigDecimal get() {
        return instance.getValue();
    }

    public static void set(BigDecimal value) {
        instance.setValue(value);
    }
}

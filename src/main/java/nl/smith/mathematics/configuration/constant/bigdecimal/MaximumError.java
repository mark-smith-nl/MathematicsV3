package nl.smith.mathematics.configuration.constant.bigdecimal;

import nl.smith.mathematics.configuration.constant.ConstantConfiguration;

import java.math.BigDecimal;

public class MaximumError extends ConstantConfiguration<BigDecimal> {

    private static final MaximumError instance = new MaximumError();

    private MaximumError() {
        super("nl.smith.mathematics.configuration.constant.error", BigDecimal.class);
    }

    public static BigDecimal get() {
        return instance.getValue();
    }

    public static void set(BigDecimal value) {
        instance.setValue(value);
    }

}

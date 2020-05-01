package nl.smith.mathematics.configuration.constant.rationalnumber;

import nl.smith.mathematics.configuration.constant.ConstantConfiguration;
import nl.smith.mathematics.numbertype.RationalNumber;

public class MaximumError extends ConstantConfiguration<RationalNumber> {

    private static final MaximumError instance = new MaximumError();

    private MaximumError() {
        super("nl.smith.mathematics.configuration.constant.error", RationalNumber.class);
    }

    public static RationalNumber get() {
        return instance.getValue();
    }

    public static void set(RationalNumber value) {
        instance.setValue(value);
    }

}

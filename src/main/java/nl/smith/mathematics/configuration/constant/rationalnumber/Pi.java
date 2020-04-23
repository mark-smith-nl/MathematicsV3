package nl.smith.mathematics.configuration.constant.rationalnumber;

import nl.smith.mathematics.configuration.constant.ConstantConfiguration;
import nl.smith.mathematics.numbertype.RationalNumber;

public class Pi extends ConstantConfiguration<RationalNumber> {

    private static final Pi instance = new Pi();

    private Pi() {
        super("nl.smith.mathematics.configuration.constant.Pi", RationalNumber.class);
    }

    public static RationalNumber get() {
        return instance.getValue();
    }

    public static void set(RationalNumber value) {
        instance.setValue(value);
    }
}

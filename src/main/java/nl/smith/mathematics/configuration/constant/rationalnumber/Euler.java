package nl.smith.mathematics.configuration.constant.rationalnumber;

import nl.smith.mathematics.configuration.constant.ConstantConfiguration;
import nl.smith.mathematics.numbertype.RationalNumber;

public class Euler extends ConstantConfiguration<RationalNumber> {

    private static final Euler instance = new Euler();

    private Euler() {
        super("nl.smith.mathematics.configuration.constant.Euler", RationalNumber.class);
    }

    public static RationalNumber get() {
        return instance.getValue();
    }

    public static void set(RationalNumber value) {
        instance.setValue(value);
    }

}

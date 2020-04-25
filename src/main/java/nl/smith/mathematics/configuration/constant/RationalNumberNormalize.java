package nl.smith.mathematics.configuration.constant;

/**
 * Utility class to specify if a {@link nl.smith.mathematics.numbertype.RationalNumber} should be normalized during construction.
 * If set to true the creation of a rational number using new RationalNumber(2, 4)
 * results in a rational mumber with numerator 1 and denominator 2.
 */
public class RationalNumberNormalize extends ConstantConfiguration<Boolean> {

    private static final RationalNumberNormalize instance = new RationalNumberNormalize();

    private RationalNumberNormalize() {
        super(Boolean.class);
    }

    public static Boolean get() {
        return instance.getValue();
    }

    public static void set(Boolean value) {
        instance.setValue(value);
    }

}

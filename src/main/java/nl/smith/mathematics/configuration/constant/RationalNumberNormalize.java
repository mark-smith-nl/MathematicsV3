package nl.smith.mathematics.configuration.constant;

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

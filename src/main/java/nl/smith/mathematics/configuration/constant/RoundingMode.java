package nl.smith.mathematics.configuration.constant;

public class RoundingMode extends ConstantConfiguration<java.math.RoundingMode> {

    private static final RoundingMode instance = new RoundingMode();

    private RoundingMode() {
        super(java.math.RoundingMode.class);
    }

    public static java.math.RoundingMode get() {
        return instance.getValue();
    }

    public static void set(java.math.RoundingMode value) {
        instance.setValue(value);
    }

}

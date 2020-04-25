package nl.smith.mathematics.configuration.constant;

/**
 * Utility class to specify the scale (i.e. the number of decimals after the decimal point)
 * of a division of two numbers ({@link Number}).
 */
public class Scale extends ConstantConfiguration<Integer> {

    private static final Scale instance = new Scale();

    private Scale() {
        super(Integer.class);
    }

    public static Integer get() {
        return instance.getValue();
    }

    public static void set(Integer value) {
        instance.setValue(value);
    }

}

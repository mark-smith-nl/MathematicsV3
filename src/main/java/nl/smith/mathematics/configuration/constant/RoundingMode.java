package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.exception.StringToConstantConfigurationException;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Utility class to specify how a division of two numbers ({@link Number}) should be rounded.
  */
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

    public static Set<String> values() {
        return Arrays.stream(java.math.RoundingMode.values()).map(Enum::name).collect(Collectors.toCollection(TreeSet::new));
    }

    public static void set(String value) throws StringToConstantConfigurationException {
        if (!values().contains(value)) throw new StringToConstantConfigurationException(RoundingMode.class, value, values());
        set(java.math.RoundingMode.valueOf(value));
    }


    public static String getDescription() {
        return "Specification how numbers are rounded.";
    }

    public static String getLabel() {
        return "Rounding";
    }

}

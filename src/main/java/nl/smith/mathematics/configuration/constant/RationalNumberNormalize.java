package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.exception.StringToConstantConfigurationException;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Utility class to specify if a {@link nl.smith.mathematics.numbertype.RationalNumber} should be normalized during construction.
 * If set to true the creation of a rational number using new RationalNumber(2, 4)
 * results in a rational number with numerator 1 and denominator 2.
 */
public class RationalNumberNormalize extends ConstantConfiguration<RationalNumberNormalize.PredefinedType> {

    public enum PredefinedType {
        YES("Normalize rational numbers. Constructing a rational number using 5/10 results in the rational number 1/10."),
        NO("Do not normalize rational numbers. Constructing a rational number using 5/10 does not result in the rational number 1/10.");

        private final String description;

        PredefinedType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private static final RationalNumberNormalize instance = new RationalNumberNormalize();

    private static RationalNumberNormalize threadValue() {
        return instance;
    }

    private RationalNumberNormalize() {
        super(PredefinedType.class);
    }

    public static PredefinedType get() {
        return instance.getValue();
    }

    public static void set(PredefinedType value) {
        instance.setValue(value);
    }

    public static Set<String> values() {
        return Arrays.stream(PredefinedType.values()).map(Enum::name).collect(Collectors.toCollection(TreeSet::new));
    }

    public static void set(String value) throws StringToConstantConfigurationException {
        if (!values().contains(value)) throw new StringToConstantConfigurationException(PredefinedType.class, value, values());
        set(PredefinedType.valueOf(value));
    }

    public static String getDescription() {
        return "Specification whether rational numbers are normalized.";
    }

    public static String getLabel() {
        return "Normalisation";
    }

}

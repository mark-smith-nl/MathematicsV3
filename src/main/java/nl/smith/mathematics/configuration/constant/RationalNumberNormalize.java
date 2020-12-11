package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.exception.StringToConstantConfigurationException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class to specify if a {@link nl.smith.mathematics.numbertype.RationalNumber} should be normalized during construction.
 * If set to true the creation of a rational number using new RationalNumber(2, 4)
 * results in a rational number with numerator 1 and denominator 2.
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

    public static Set<String> values() {
        return Stream.of(Boolean.TRUE, Boolean.FALSE).map(Object::toString).collect(Collectors.toSet());
    }

    public static void set(String value) throws StringToConstantConfigurationException {
        if (!values().contains(value)) throw new StringToConstantConfigurationException(RationalNumberOutputType.PredefinedType.class, value, values());
        set(Boolean.valueOf(value));
    }
}

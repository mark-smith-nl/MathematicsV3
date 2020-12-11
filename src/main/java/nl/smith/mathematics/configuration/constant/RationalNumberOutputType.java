package nl.smith.mathematics.configuration.constant;


import nl.smith.mathematics.exception.StringToConstantConfigurationException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class RationalNumberOutputType extends ConstantConfiguration<RationalNumberOutputType.PredefinedType> {

    public enum PredefinedType {
        COMPONENTS("Represent exactly a rational number as <numerator>/<denominator>. " +
                "For instance 1051/495 for rational number 1051/495."),
        EXACT("Represents exactly a rational number using different string components. " +
                "For instance 2.1[23]R for rational number 1051/495."),
        COMPONENTS_AND_EXACT("Represent a rational number using " + COMPONENTS + " and " + EXACT +
                "For instance: 1051/495 ---> 2.1[23]R for rational number 1051/495."),
        TRUNCATED("Representation of a rational number truncated using the specified scale." +
                "For instance 2.1 for rational number 1051/495 using scale 1."),
        ALL("Represent a rational number using " + COMPONENTS + ", " + EXACT + " and " + TRUNCATED +
                " For instance 1051/495 ---> 2.1[23]R ~ 2.1 for rational number 1051/495 using scale 1.");

        private final String description;

        PredefinedType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private static final RationalNumberOutputType instance = new RationalNumberOutputType();

    private RationalNumberOutputType() {
        super(PredefinedType.class);
    }

    public static PredefinedType get() {
        return instance.getValue();
    }

    public static void set(PredefinedType value) {
        instance.setValue(value);
    }

    public static Set<String> values() {
        return Arrays.stream(PredefinedType.values()).map(Enum::name).collect(Collectors.toSet());
    }

    public static void set(String value) throws StringToConstantConfigurationException {
        if (!values().contains(value)) throw new StringToConstantConfigurationException(PredefinedType.class, value, values());
        set(PredefinedType.valueOf(value));
    }

}

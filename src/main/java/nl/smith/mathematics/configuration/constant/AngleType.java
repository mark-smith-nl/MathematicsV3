package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.exception.StringToConstantConfigurationException;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class AngleType extends ConstantConfiguration<AngleType.PredefinedType> {

    public enum PredefinedType {
        DEG ("Degrees"),
        GRAD("Gradients"),
        RAD("Radians");

        private final String description;

        PredefinedType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private static final AngleType instance = new AngleType();

    private AngleType() {
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
        if (!values().contains(value)) throw new StringToConstantConfigurationException(RationalNumberOutputType.PredefinedType.class, value, values());
        set(AngleType.PredefinedType.valueOf(value));
    }

    public static String getDescription() {
        return "Specification the way angles are specified.";
    }


    public static String getLabel() {
        return "Angle";
    }
}

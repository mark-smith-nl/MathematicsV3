package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.exception.StringToConstantConfigurationException;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

//TODO Javadoc
public abstract class EnumConstantConfiguration<T extends Enum<?> & EnumConstant> extends ConstantConfiguration<T> {

    public EnumConstantConfiguration(Class<T> valueTypeClass) {
        super(valueTypeClass);
    }

    private Class<T> getEnumClass() {
        Class<?>[] declaredClasses = getClass().getDeclaredClasses();
        if (declaredClasses.length == 1 && declaredClasses[0].isEnum()) return (Class<T>) declaredClasses[0];

        throw new IllegalStateException();
    }

    public Set<T> withSiblings() {
        return Arrays.stream(getEnumClass().getEnumConstants()).collect(Collectors.toCollection(TreeSet::new));
    }

    public Map<T, String> valuesWithDescriptions() {
        return Arrays.stream(getEnumClass().getEnumConstants()).collect(Collectors.toMap(v -> v, EnumConstant::valueDescription));
    }

    public Set<String> valuesAsString() {
        return Arrays.stream(getEnumClass().getEnumConstants()).map(Enum::name).collect(Collectors.toCollection(TreeSet::new));
    }

    public Map<String, String> withSiblingsAsStringWithDescriptions() {
        return Arrays.stream(getEnumClass().getEnumConstants()).collect(Collectors.toMap(Enum::name, EnumConstant::valueDescription));
    }

    public void set(String stringValue) throws StringToConstantConfigurationException {
        for (T value : withSiblings()) {
            if (value.name().equals(stringValue)) {
                set(value);
                return;
            }
        }

        throw new StringToConstantConfigurationException(RationalNumberOutputType.PredefinedType.class, stringValue, valuesAsString());

    }

    public static class AngleType extends EnumConstantConfiguration<AngleType.PredefinedType> {

        public enum PredefinedType implements EnumConstant {

            DEG("Degrees"),
            GRAD("Gradients"),
            RAD("Radians");

            private final String description;

            PredefinedType(String description) {
                this.description = description;
            }

            public String valueDescription() {
                return description;
            }
        }

        private static final AngleType instance = new AngleType();

        public AngleType() {
            super(PredefinedType.class);
        }

        public static AngleType value() {
            return instance;
        }

        @Override
        public String constantDescription() {
            return "Specification the way angles are specified.";
        }

        @Override
        public String name() {
            return "Angle";
        }

    }

    public static class RationalNumberNormalize extends EnumConstantConfiguration<RationalNumberNormalize.PredefinedType> {

        public enum PredefinedType implements EnumConstant {

            YES("Normalize rational numbers. Constructing a rational number using 5/10 results in the rational number 1/10."),
            NO("Do not normalize rational numbers. Constructing a rational number using 5/10 does not result in the rational number 1/10.");

            private final String description;

            PredefinedType(String description) {
                this.description = description;
            }

            public String valueDescription() {
                return description;
            }
        }

        private static final RationalNumberNormalize instance = new RationalNumberNormalize();


        private RationalNumberNormalize() {
            super(PredefinedType.class);
        }

        public static RationalNumberNormalize value() {
            return instance;
        }

        @Override
        public String constantDescription() {
            return "Specification whether rational numbers are normalized.";
        }

        @Override
        public String name() {
            return "Normalisation";
        }

    }

    public static class RationalNumberOutputType extends EnumConstantConfiguration<RationalNumberOutputType.PredefinedType> {

        public enum PredefinedType implements EnumConstant {

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

            public String valueDescription() {
                return description;
            }
        }

        private static final RationalNumberOutputType instance = new RationalNumberOutputType();


        private RationalNumberOutputType() {
            super(PredefinedType.class);
        }

        public static RationalNumberOutputType value() {
            return instance;
        }

        @Override
        public String constantDescription() {
            return "Specification how rational numbers are displayed.";
        }

        @Override
        public String name() {
            return "Output";
        }

    }

    public static class RoundingMode extends EnumConstantConfiguration<RoundingMode.PredefinedType> {

        public enum PredefinedType implements EnumConstant {

            UP("5.5 ---> 6, 2.5 ---> 3, 1.6 ---> 2, 1.1 ---> 2, 1.0 --->1 , -1.0 ---> -1, -1.1 ---> -2, -1.6 ---> -2, -2.5 ---> -3, -5.5 ---> -6", java.math.RoundingMode.UP),
            DOWN("5.5 ---> 5, 2.5 ---> 2, 1.6 ---> 1, 1.1 ---> 1, 1.0 ---> 1, -1.0 ---> -1, -1.1 ---> -1, -1.6 ---> -1, -2.5 ---> -2, -5.5 ---> -5", java.math.RoundingMode.DOWN),
            CEILING("5.5 ---> 6, 2.5 ---> 3, 1.6 ---> 2, 1.1 ---> 2, 1.0 ---> 1, -1.0 ---> -1, -1.1 ---> -1, -1.6 ---> -1, -2.5 ---> -2, -5.5 ---> -5", java.math.RoundingMode.CEILING),
            FLOOR("5.5 ---> 5, 2.5 ---> 2, 1.6 ---> 1, 1.1 ---> 1, 1.0 ---> 1, -1.0 ---> -1, -1.1 ---> -2, -1.6 ---> -2, -2.5 ---> -3, -5.5 ---> -6", java.math.RoundingMode.FLOOR),
            HALF_UP("5.5 ---> 6, 2.5 ---> 3, 1.6 ---> 2, 1.1 ---> 1, 1.0 ---> 1, -1.0 ---> -1, -1.1 ---> -1, -1.6 ---> -2, -2.5 ---> -3, -5.5 ---> -6", java.math.RoundingMode.HALF_UP),
            HALF_DOWN("5.5 ---> 5, 2.5 ---> 2, 1.6 ---> 2, 1.1 ---> 1, 1.0 ---> 1, -1.0 ---> -1, -1.1 ---> -1, -1.6 ---> -2, -2.5 ---> -2, -5.5 ---> -5", java.math.RoundingMode.HALF_DOWN),
            HALF_EVEN("5.5 ---> 6, 2.5 ---> 2, 1.6 ---> 2, 1.1 ---> 1, 1.0 ---> 1, -1.0 ---> -1, -1.1 ---> -1, -1.6 ---> -2, -2.5 ---> -2, -5.5 ---> -6", java.math.RoundingMode.HALF_EVEN),
            UNNECESSARY("5.5 ---> ArithmeticException, 2.5 ---> ArithmeticException, 1.6 ---> ArithmeticException, 1.1 ---> ArithmeticException, 1.0 ---> 1, -1.0 ---> -1, -1.1 ---> ArithmeticException, -1.6 ---> ArithmeticException, -2.5 ---> ArithmeticException, -5.5 ---> ArithmeticException", java.math.RoundingMode.UNNECESSARY);

            private final String description;

            private final java.math.RoundingMode mathRoundingMode;

            PredefinedType(String description, java.math.RoundingMode mathRoundingMode) {
                this.description = description;
                this.mathRoundingMode = mathRoundingMode;
            }

            public String valueDescription() {
                return description;
            }

            public java.math.RoundingMode mathRoundingMode() {
                return mathRoundingMode;
            }

        }

        private static final RoundingMode instance = new RoundingMode();


        private RoundingMode() {
            super(PredefinedType.class);
        }

        public static RoundingMode value() {
            return instance;
        }

        @Override
        public String constantDescription() {
            return "Specification how numbers are rounded.";
        }

        @Override
        public String name() {
            return "Rounding";
        }

    }

}

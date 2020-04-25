package nl.smith.mathematics.configuration.constant;

/**
 * Utility class to specify how an instance of a {@link nl.smith.mathematics.numbertype.RationalNumber} should be presented.
 * See {@link nl.smith.mathematics.numbertype.RationalNumber#toString()}.
*/
public class RationalNumberOutputType extends ConstantConfiguration<RationalNumberOutputType.Type> {

    public enum Type {
        COMPONENTS("Represent exactly a rational number as <numerator>/<denominator>. " +
                "For instance 1051/495 for rational number 1051/495."),
        EXACT("Represents exactly a rational a number using different string components. " +
                "For instance 2.1{23}R for rational number 1051/495."),
        COMPONENTS_AND_EXACT("Represent a rational number using " + COMPONENTS + " and " + EXACT +
                "For instance: 1051/495 ---> 2.1{23}R for rational number 1051/495."),
        TRUNCATED("Representation of a rational number truncated using the specified scale." +
                "For instance 2.1 for rational number 1051/495 using scale 1."),
        ALL("Represent a rational number using " + COMPONENTS + ", " + EXACT + " and " + TRUNCATED +
                "For instance 1051/495 ---> 2.1{23}R ~ 2.1 for rational number 1051/495 using scale 1.");

        private final String description;

        Type(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private static final RationalNumberOutputType instance = new RationalNumberOutputType();

    private RationalNumberOutputType() {
        super(RationalNumberOutputType.Type.class);
    }

    public static RationalNumberOutputType.Type get() {
        return instance.getValue();
    }

    public static void set(RationalNumberOutputType.Type value) {
        instance.setValue(value);
    }

}

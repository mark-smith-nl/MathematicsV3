package nl.smith.mathematics.configuration.constant;

public class AngleType extends ConstantConfiguration<AngleType.Type> {

    public enum Type {
        DEG ("Degrees"),
        GRAD("Gradients"),
        RAD("Radians");

        private final String description;

        Type(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private static final AngleType instance = new AngleType();

    private AngleType() {
        super(AngleType.Type.class);
    }

    public static AngleType.Type get() {
        return instance.getValue();
    }

    public static void set(AngleType.Type value) {
        instance.setValue(value);
    }

}

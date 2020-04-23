package nl.smith.mathematics.configuration.constant;

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

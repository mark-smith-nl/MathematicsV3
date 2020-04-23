package nl.smith.mathematics.configuration.constant;

public class TaylorDegreeOfPolynom extends ConstantConfiguration<Integer> {

    private static final TaylorDegreeOfPolynom instance = new TaylorDegreeOfPolynom();

    private TaylorDegreeOfPolynom() {
        super(Integer.class);
    }

    public static Integer get() {
        return instance.getValue();
    }

    public static void set(Integer value) {
        instance.setValue(value);
    }

}

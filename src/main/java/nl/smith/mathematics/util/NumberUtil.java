package nl.smith.mathematics.util;

import static java.lang.String.format;

public class NumberUtil {

    private static final String NUMBER_FACTORY_METHOD_NAME = "valueOf";

    private NumberUtil() {
        throw new IllegalStateException(format("Can not instantiate %s", this.getClass().getCanonicalName()));
    }

    /**
     * Method to convert a specified string value to a corresponding instance of the specified class.
     *
     * @param stringNumber The value to be converted
     * @param clazz        The class which should be instantiated
     * @param <T>          Class extending {@link Number}
     * @return An instance of the specified class using the specified string value
     * @throws IllegalArgumentException if the supplied is null or empty or if the specified class does not extend {@link Number}
     */
    public static <T extends Number> T valueOf(String stringNumber, Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("A class should be specified.");
        }

        if (clazz.isPrimitive()) {
            clazz = (Class<T>) StringToObjectUtil.getPrimitiveClassToWrapperClass(clazz);
        }

        if (!Number.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(format("Can not determine number value.\nSpecified class %s does not extend %s", clazz.getCanonicalName(), Number.class.getCanonicalName()));
        }

        return StringToObjectUtil.valueOf(stringNumber, clazz);

    }

}

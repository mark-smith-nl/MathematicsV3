package nl.smith.mathematics.util;

import nl.smith.mathematics.numbertype.ArithmeticFunctions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NumberUtil {

    private static final String NUMBER_FACTORY_METHOD_NAME = "valueOf";

    private static final Set<? extends Class<? extends Number>> NUMBER_CLASSES_IMPLEMENTING_VALUEOF_USING_STRING = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Double.class,
            Float.class,
            ArithmeticFunctions.class)));

    private static final Set<? extends Class<? extends Number>> NUMBER_CLASSES_IMPLEMENTING_CONSTRUCTOR_USING_STRING = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            BigInteger.class,
            BigDecimal.class)));

    private static final Class<?>[] NUMBER_FACTORY_METHOD_ARGUMENT_TYPES = new Class<?>[]{String.class};

    private NumberUtil() {
        throw new IllegalStateException(String.format("Can not instantiate %s", this.getClass().getCanonicalName()));
    }

    public static boolean isNumber(Object o) {
        boolean isValid = true;

        if (o != null) {
            isValid = Number.class.isAssignableFrom(o.getClass());
        }

        return isValid;
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
        if (stringNumber == null || stringNumber.isEmpty()) {
            throw new IllegalArgumentException(String.format("Can not determine number value.\nNo string value specified to be turned into an instance of %s.", clazz.getCanonicalName()));
        }

        if (clazz.isPrimitive()) {
            clazz = (Class<T>) getPrimitiveClassToWrapperClass(clazz);
        }

        if (!Number.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(String.format("Can not determine number value.\nSpecified class %s does not extend %s", clazz.getCanonicalName(), Number.class.getCanonicalName()));
        }

        for (Class<? extends Number> numberClazz : NUMBER_CLASSES_IMPLEMENTING_VALUEOF_USING_STRING) {
            if (numberClazz.isAssignableFrom(clazz)) {
                try {
                    Method valueOf = clazz.getDeclaredMethod(NUMBER_FACTORY_METHOD_NAME, NUMBER_FACTORY_METHOD_ARGUMENT_TYPES);
                    return (T) valueOf.invoke(null, stringNumber);
                } catch (Exception e) {
                    String arguments = Stream.of(NUMBER_FACTORY_METHOD_ARGUMENT_TYPES).map(Class::getCanonicalName).collect(Collectors.joining(", "));
                    throw new IllegalStateException(String.format("Can not invoke method %s.%s(%s)", clazz.getCanonicalName(), NUMBER_FACTORY_METHOD_NAME, arguments));
                }

            }
        }

        for (Class<? extends Number> numberClazz : NUMBER_CLASSES_IMPLEMENTING_CONSTRUCTOR_USING_STRING) {
            if (numberClazz.isAssignableFrom(clazz)) {
                try {
                    Constructor<T> constructor = clazz.getConstructor(NUMBER_FACTORY_METHOD_ARGUMENT_TYPES);
                    return constructor.newInstance(new Object[]{stringNumber});
                } catch (Exception e) {
                    String arguments = Stream.of(NUMBER_FACTORY_METHOD_ARGUMENT_TYPES).map(Class::getCanonicalName).collect(Collectors.joining(", "));
                    throw new IllegalStateException(String.format("Can not invoke constructor %s(%s)", clazz.getCanonicalName(), arguments));
                }
            }

        }

        throw new IllegalStateException(String.format("The conversion from a string into an instance of %s is not supported.", clazz.getCanonicalName()));
    }

    /** Method returns the corrsponding wrapper class of a primitive class
     * or null if no class is specified or if the class is not a primitive class. */
    public static Class<?> getPrimitiveClassToWrapperClass(Class<?> clazz) {
        if (clazz == null || !clazz.isPrimitive()) {
            return null;
        }

        if (clazz == byte.class) {
            return Byte.class;
        } else if (clazz == short.class) {
            return Short.class;
        } else if (clazz == int.class) {
            return Integer.class;
        } else if (clazz == long.class) {
            return Long.class;
        } else if (clazz == double.class) {
            return Double.class;
        } else if (clazz == float.class) {
            return Float.class;
        } else if (clazz == char.class) {
            return Character.class;
        }else if (clazz == boolean.class) {
            return Boolean.class;
        }

        return Void.class;

    }
}

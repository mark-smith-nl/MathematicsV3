package nl.smith.mathematics.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringToObjectUtil {

    private static final String FACTORY_METHOD_NAME = "valueOf";

    private static final Class<?>[] FACTORY_METHOD_ARGUMENT_TYPES = new Class<?>[]{String.class};

    private StringToObjectUtil() {
        throw new IllegalStateException(String.format("Can not instantiate %s", this.getClass().getCanonicalName()));
    }

    public static <T> T valueOf(String stringValue, Class<T> clazz) {
        if (stringValue == null || stringValue.isEmpty() || clazz == null) {
            throw new IllegalArgumentException("String value and class should be specified.");
        }

        if (clazz.isPrimitive()) {
            clazz = (Class<T>) getPrimitiveClassToWrapperClass(clazz);
        }

        Method valueOf = Stream.of(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals(FACTORY_METHOD_NAME))
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .filter(m -> Arrays.equals(m.getParameterTypes(), FACTORY_METHOD_ARGUMENT_TYPES))
                .findFirst()
                .orElse(null);
        if (valueOf != null) {
            try {
                return (T) valueOf.invoke(null, stringValue);
            } catch (Exception e) {
                String arguments = Stream.of(FACTORY_METHOD_ARGUMENT_TYPES).map(Class::getCanonicalName).collect(Collectors.joining(", "));
                String message ;
                if (e instanceof InvocationTargetException) {
                    message = ((InvocationTargetException) e).getTargetException().getMessage();
                } else {
                    message = e.getMessage();
                }

                throw new IllegalStateException(String.format("Can not invoke method %s.%s(%s) using %s.%n Errormessage: %s", clazz.getCanonicalName(), FACTORY_METHOD_NAME, arguments, stringValue, message));
            }
        }

        Constructor<?> constructor = Stream.of(clazz.getDeclaredConstructors())
                .filter(c -> Modifier.isPublic(c.getModifiers()))
                .filter(c -> Arrays.equals(c.getParameterTypes(), FACTORY_METHOD_ARGUMENT_TYPES))
                .findFirst()
                .orElse(null);

        if (constructor != null) {
            try {
                return (T) constructor.newInstance(new Object[]{stringValue});
            } catch (Exception e) {
                String arguments = Stream.of(FACTORY_METHOD_ARGUMENT_TYPES).map(Class::getCanonicalName).collect(Collectors.joining(", "));
                throw new IllegalStateException(String.format("Can not invoke constructor %s(%s) using %s", clazz.getCanonicalName(), arguments, stringValue));
            }
        }

        throw new IllegalStateException(String.format("Can not convert %s(%s) to an instance of %s.", stringValue, String.class.getCanonicalName(), clazz.getCanonicalName()));
    }

    /** Method returns the corresponding wrapper class of a primitive class
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

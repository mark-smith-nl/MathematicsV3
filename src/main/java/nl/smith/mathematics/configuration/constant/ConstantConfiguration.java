package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.exception.StringToConstantConfigurationException;
import nl.smith.mathematics.util.StringToObjectUtil;
import nl.smith.mathematics.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * <pre>
 * Default values for all implementing classes are specified in {@link ConstantConfiguration#PROPERTY_FILE_NAME}.
 * The specified values are associated with the thread in which the value is set.
 * Instances of this class contain default values which can not be modified.
 * The method {@link ConstantConfiguration#getValue()} retrieves the value from the current thread or if it is not availabe retrieves the default value.
 *
 * Setting and retrieving values can be done using:
 *          - static methods in the concrete implementing class but also
 *          - in the abstract class by the static method {@link ConstantConfiguration#getValue(Class)}.
 *
 * Values are enums either:
 *          - stateless (only the enum value is specified to set the value) or
 *          - stateful (the enum itself has an immutable value of type {@link Number} which is set during creation.)
 *
 *</pre>
 * @param <T>
 */
public abstract class ConstantConfiguration<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstantConfiguration.class);

    public static final String PROPERTY_FILE_NAME = "application.properties";

    private final T defaultValue;

    private final String propertyName;

    private final Class<T> valueTypeClass;

    private static Set<ConstantConfiguration<?>> constants = new HashSet<>();

    public ConstantConfiguration(Class<T> valueTypeClass) {
        this.propertyName = getClass().getCanonicalName();
        this.valueTypeClass = valueTypeClass;
        defaultValue = getDefaultValueFromSystem();
        constants.add(this);

    }

    public ConstantConfiguration(Class<T> valueTypeClass, String propertyName) {
        this.propertyName = propertyName;
        this.valueTypeClass = valueTypeClass;
        defaultValue = getDefaultValueFromSystem();
        constants.add(this);
    }

    public void setValue(T value) {
        ThreadContext.setValue(propertyName, value);
    }

    public T getValue() {
        return (T) ThreadContext.getValue(propertyName).orElseGet(this::defaultValue);
    }

    public static Set<String> values(Class<? extends ConstantConfiguration<?>> configurationClass) {
        try {
            Object values = configurationClass.getDeclaredMethod("values").invoke(null);
            return (Set<String>) values;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void setValue(Class<? extends ConstantConfiguration<?>> configurationClass, String value) throws StringToConstantConfigurationException {
        try {
            LOGGER.info("Set configuration {} {} ---> {} ", configurationClass.getCanonicalName(), getValue(configurationClass), value);
            configurationClass.getDeclaredMethod("set", new Class[]{String.class}).invoke(null, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new StringToConstantConfigurationException(configurationClass, value, values(configurationClass));
        }
    }

    public static String getValue(Class<? extends ConstantConfiguration<?>> configurationClass) {
        String methodName = "get";
        try {
            return configurationClass.getDeclaredMethod(methodName).invoke(null).toString();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(String.format("Static method %s.%s() not found.", configurationClass.getCanonicalName(), methodName));
        }
    }

    public static String getDescription(Class<? extends ConstantConfiguration<?>> configurationClass) {
        String methodName = "getDescription";
        try {
            return configurationClass.getDeclaredMethod(methodName).invoke(null).toString();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(String.format("Static method %s.%s() not found.", configurationClass.getCanonicalName(), methodName));
        }
    }

    public static String getLabel(Class<? extends ConstantConfiguration<?>> configurationClass) {
        String methodName = "getLabel";
        try {
            return configurationClass.getDeclaredMethod(methodName).invoke(null).toString();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(String.format("Static method %s.%s() not found.", configurationClass.getCanonicalName(), methodName));
        }
    }

    private T defaultValue() {
        return defaultValue;
    }

    private T getDefaultValueFromSystem() {
        String value = System.getProperty(propertyName);
        if (value == null) {
            try {
                Properties properties = new Properties();
                properties.load(new ClassPathResource(PROPERTY_FILE_NAME).getInputStream());
                value = (String) properties.get(propertyName);
            } catch (IOException e) {

            }
        }
        if (StringUtils.isEmpty(value)) {
            throw new IllegalStateException(format("\nNo default value specified for property %s." +
                    "\nPlease specify this value as a system property -D%1$s=<value>" +
                    "\nor specify it as %1$s=<value> in file %2$s.", propertyName, PROPERTY_FILE_NAME));
        }

        return StringToObjectUtil.valueOf(value, valueTypeClass);
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public Class<T> getValueTypeClass() {
        return valueTypeClass;
    }

    public static List<?> getEnumConstants() {
        return constants.stream()
                .filter(constant -> Enum.class.isAssignableFrom(constant.getValueTypeClass()))
                .sorted(Comparator.comparing(c -> c.getClass().getSimpleName()))
                .collect(Collectors.toList());
    }
}

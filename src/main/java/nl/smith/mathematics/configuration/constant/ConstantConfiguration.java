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
import java.util.Properties;
import java.util.Set;

import static java.lang.String.format;

/**
 * <pre>
 * Default values for all implementing classes are specified in {@link ConstantConfiguration#PROPERTY_FILE_NAME}.
 * The specified values are associated with the thread in which the value is set.
 *
 * Setting and retrieving values can be done using:
 *          - static methods in the concrete implementing class but also
 *          - in the abstract class by the static method {@link ConstantConfiguration#getValue(Class)}.
 *
 * Values are enums either:
 *          - stateless (only the enum value is specified to set the value) or
 *          - stateful (the enum itself has a value which is set)
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

    public ConstantConfiguration(Class<T> valueTypeClass) {
        this.propertyName = getClass().getCanonicalName();
        this.valueTypeClass = valueTypeClass;
        defaultValue = getDefaultValueFromSystem();
    }

    public ConstantConfiguration(Class<T> valueTypeClass, String propertyName) {
        this.propertyName = propertyName;
        this.valueTypeClass = valueTypeClass;
        defaultValue = getDefaultValueFromSystem();
    }

    public void setValue(T value) {
        ThreadContext.setValue(propertyName, value);
    }

    public T getValue() {
        return (T) ThreadContext.getValue(propertyName).orElseGet(this::defaultValue);
    }

    public static Set<String> values(Class<? extends ConstantConfiguration<?>> configurationClass) {
        try {
            Object values = configurationClass.getDeclaredMethod("values", new Class[0]).invoke(null);
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

    public static String getValue(Class<? extends ConstantConfiguration> configurationClass) {
        try {
            return configurationClass.getDeclaredMethod("get").invoke(null, new Object[0]).toString();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException();
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

    public String getPropertyName() {
        return propertyName;
    }

    public Class<T> getValueTypeClass() {
        return valueTypeClass;
    }
}

package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.util.StringToObjectUtil;
import nl.smith.mathematics.util.UserSystemContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

import static java.lang.String.format;

/**
 * Default values for all implementing classes are specified in {@link ConstantConfiguration#PROPERTY_FILE_NAME}.
 * The specified values are associated with the thread in which the value is set.
 * Setting and retrieval of a value can be done using static methods in the implementing class.
 * @param <T>
 */
public abstract class ConstantConfiguration<T> {

    public static final String PROPERTY_FILE_NAME = "application.properties";

    private final T defaultValue;

    private final String propertyName;

    private final Class<T> clazz;

    public ConstantConfiguration(Class<T> clazz) {
        this.propertyName = getClass().getCanonicalName();
        this.clazz = clazz;
        defaultValue = getDefaultValueFromSystem();
    }

    public ConstantConfiguration(String propertyName, Class<T> clazz) {
        this.propertyName = propertyName;
        this.clazz = clazz;
        defaultValue = getDefaultValueFromSystem();
    }

    public void setValue(T value) {
        UserSystemContext.setValue(propertyName, value);
    }

    public T getValue() {
        return (T) UserSystemContext.getValue(propertyName).orElseGet(this::defaultValue);
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

        return StringToObjectUtil.valueOf(value, clazz);
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Class<T> getClazz() {
        return clazz;
    }
}

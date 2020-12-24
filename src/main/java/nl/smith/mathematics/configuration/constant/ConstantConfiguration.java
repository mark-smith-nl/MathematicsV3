package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.util.StringToObjectUtil;
import nl.smith.mathematics.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static java.lang.String.format;

/**
 * <pre>
 * Default values for all implementing classes are specified in {@link ConstantConfiguration#PROPERTY_FILE_NAME}.
 * The specified values are associated with the thread in which the value is set.
 * Instances of this class contain default values which can not be modified.
 * The method {@link ConstantConfiguration#get()} retrieves the value from the current thread or if it is not availabe retrieves the default value.
 *
 * Setting and retrieving values can be done using:
 *          - static methods in the concrete implementing class but also
 *          - in the abstract class by the static method {@link ConstantConfiguration#get(Class)}.
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

    private static final Set<EnumConstantConfiguration<?>> enumConstantInstances = new HashSet<>();

    public <TE extends Enum & EnumConstant> ConstantConfiguration(Class<TE> valueTypeClass) {
        this.propertyName = getClass().getCanonicalName();
        this.valueTypeClass = (Class<T>) valueTypeClass;
        defaultValue = getDefaultValueFromSystem();
        enumConstantInstances.add((EnumConstantConfiguration<?>) this);
    }

    public <N extends Number> ConstantConfiguration(Class<N> valueTypeClass, String propertyName) {
        this.propertyName = propertyName;
        this.valueTypeClass = (Class<T>) valueTypeClass;
        defaultValue = getDefaultValueFromSystem();
    }


    //TODO Activate and test
    /*public static String getValue(Class<? extends ConstantConfiguration<?>> clazz) {
        try {
            Method method = clazz.getDeclaredMethod("value");
            Object instance = method.invoke(null);
            method = clazz.getDeclaredMethod("get");
            return method.invoke(instance).toString();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(format("Can not retrieve value for class %s using reflection.", clazz.getCanonicalName()));
        }
    }*/

    public void set(T value) {
        ThreadContext.setValue(propertyName, value);
    }

    public T get() {
        return (T) ThreadContext.getValue(propertyName).orElseGet(this::defaultValue);
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

    public abstract String constantDescription();

    public abstract String name();

    public static Set<EnumConstantConfiguration<?>> getEnumConstantInstances() {
        return enumConstantInstances;
    }

}

package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.util.StringToObjectUtil;
import nl.smith.mathematics.util.UserSystemContext;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.util.Properties;

public abstract class ConstantConfiguration<T> {

    private static final String PROPERTY_FILE_NAME = "classpath:application.properties";

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
        return (T) UserSystemContext.getValue(propertyName).orElse(defaultValue);
    }

    private T getDefaultValueFromSystem() {
        String value = System.getProperty(propertyName);
        if (value == null) {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(ResourceUtils.getFile(PROPERTY_FILE_NAME)));
                value = (String) properties.get(propertyName);
            } catch (Exception e) {
                //
            }
        }
        if (StringUtils.isEmpty(value)) {
            throw new IllegalStateException(String.format("\nNo default value specified for property %s." +
                    "\nPlease specify this value as a system property -D%1$s=<value>" +
                    "\nor specify it as %1$s=<value> in file %2$s.", propertyName, PROPERTY_FILE_NAME));
        }

        return StringToObjectUtil.valueOf(value, clazz);
    }

}

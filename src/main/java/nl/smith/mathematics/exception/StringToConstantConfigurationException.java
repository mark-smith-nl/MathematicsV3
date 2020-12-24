package nl.smith.mathematics.exception;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This method <description of functionality>
 *
 * @author m.smithhva.nl
 */
public class StringToConstantConfigurationException extends Exception{

    private final Class<?> clazz;

    private final String value;

    private final Set<String> acceptedValues;

    public StringToConstantConfigurationException(Class<?> clazz, String value, Set<String> acceptedValues) {
        super(String.format("Can not convert value '%s' to instance of %s.\nAccepted values: %s",
                value,
                clazz.getCanonicalName(),
                acceptedValues.stream().collect(Collectors.joining("', '", "['", "']"))));
        this.clazz = clazz;
        this.value = value;
        this.acceptedValues = acceptedValues;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getValue() {
        return value;
    }
}


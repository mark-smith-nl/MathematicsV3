package nl.smith.mathematics.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

/** Utility to store thread specific key value pairs in a map and associate them with a specific thread.
 * This class is intended to store user specific properties which can be retrieved anywhere in the thread.
 * Null values are not stored. Adding a property with a null value results in the removal of the property. */
public class UserSystemContext {

    private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void setValue(String propertyName, Object value) {
        if (value == null) {
            removeValue(propertyName);
            return;
        }

        getMap().put(propertyName, value);
    }

    public static void removeValue(String propertyName) {
        getMap().remove(propertyName);
    }

    public static void setValue(String propertyName, String stringValue, Class<?> clazz) {
        setValue(propertyName, StringToObjectUtil.valueOf(stringValue, clazz));
    }

    public static Optional<Object> getValue(String propertyName) {
        return Optional.ofNullable(getMap().get(propertyName));
    }

    public static <T> Optional<T> getValue(String propertyName, Class<T> clazz) {
        Optional<Object> value = getValue(propertyName);
        if (!value.isPresent() || clazz.isAssignableFrom(value.getClass())) {
            return (Optional<T>) value;
        }

        throw new IllegalArgumentException(format("Value for property %s was not found or was not of type %s", propertyName, clazz.getCanonicalName()));

    }
    public static <T> Map<String, T> getValueOfTypes(Class<T> clazz) {
        return (Map<String, T>) getMap().entrySet().stream()
                .filter(e -> clazz.isAssignableFrom(e.getValue().getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <T> Optional<T> getSingleValueOfType(Class<T> clazz) {
        Map<String, T> valuesOfType = (Map<String, T>) getMap().entrySet().stream()
                .filter(e -> clazz.isAssignableFrom(e.getValue().getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (valuesOfType.isEmpty()) {
            return Optional.empty();
        }

        if (valuesOfType.size() == 1) {
            return  Optional.of(valuesOfType.entrySet().iterator().next().getValue());
        }

        throw new IllegalArgumentException(format("Multiple properties found of type %s", clazz.getCanonicalName()));
    }

    public static Set<String> getPropertyNames() {
        return getMap().keySet();
    }

    public static void setValues(Map<String, Object> values) {
        threadLocal.set(values);
    }

    public static Map<String, Object> getValues() {
        return getMap();

    }

    /** Retrieves the property map associated with a thread.
     * Map can be empty but does not contain null values.
     */
    private static Map<String, Object> getMap() {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }

        return map;
    }
}

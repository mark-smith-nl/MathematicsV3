package nl.smith.mathematics.util;

/** Utility class to wrap an object.
 * The wrapper can be used in lambda expressions in which variables should be declared final.
 * Wrap the value in the, as final declared, ObjectWrapper and use setValue() to set its value.
 */
public class ObjectWrapper <T> {

    private T value;

    public ObjectWrapper(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    /** Class for wrapping an object. If the method getValue is invoked and the object is null an exception is thrown. */
    public static class NotNullObjectWrapper<T> extends ObjectWrapper <T> {

        private final Class<T> clazz;

        private final String errorMessage;

        public NotNullObjectWrapper(T value, Class<T> clazz, String errorMessage) {
            super(value);
            if (clazz == null) {
                throw new IllegalStateException(String.format("Please specify a class for constructing an instance of %s.", this.getClass().getCanonicalName()));
            }
            this.clazz = clazz;

            this.errorMessage = errorMessage ;
        }

        @Override
        public T getValue() {
            if (super.getValue() == null) {
                throw new IllegalStateException(String.format("%s%nValue is null.%nExpected a %s not null value.", errorMessage, clazz.getCanonicalName()));
            }

            return super.getValue();
        }
    }
}

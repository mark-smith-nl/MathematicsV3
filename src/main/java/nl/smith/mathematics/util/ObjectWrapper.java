package nl.smith.mathematics.util;

/** Utillity class to wrap an object.
 * The wrapper can be used in lambda expressions in which variables should be declared final.
 * Wrap the value in the as final declared ObjectWrapper and use setValue() to set its value.
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
}

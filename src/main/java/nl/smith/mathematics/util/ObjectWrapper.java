package nl.smith.mathematics.util;

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

package nl.smith.mathematics.util;

import nl.smith.mathematics.mathematicalfunctions.definition.AbstractExampleRecursiveFunctionContainer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MathematicalMethodUtilTest {

    private final Class<AbstractExampleRecursiveFunctionContainer> clazz = AbstractExampleRecursiveFunctionContainer.class;

    @Test
    void checkGenericsEnclosingClass_nullArgument() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> MathematicalMethodUtil.checkGenericsEnclosingClass(null));

        assertEquals("Please specify a class.", exception.getMessage());
    }

    @Test
    void checkGenericsEnclosingClass_notDirectlyExtendingRecursiveFunctionContainer() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> MathematicalMethodUtil.checkGenericsEnclosingClass(String.class));

        assertEquals("Improper use of generics." + lineSeparator() +
                "Define your class as public abstract <N extends Number, S extends String> extends RecursiveFunctionContainer<N, S>", exception.getMessage());
    }

    @Test
    void checkGenericsEnclosingClass() {
        MathematicalMethodUtil.checkGenericsEnclosingClass(clazz);
    }

    @Test
    void checkModifiers_nullArgument() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> MathematicalMethodUtil.checkModifiers(null));

        assertEquals("Please specify a method.", exception.getMessage());
    }

    @Test
    void checkModifiers_privateMethod() throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod("two", Number.class);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                MathematicalMethodUtil.checkModifiers(method));

        assertEquals("The method nl.smith.mathematics.mathematicalfunctions.definition.AbstractExampleRecursiveFunctionContainer.two should be public and abstract.", exception.getMessage());
    }

    @Test
    void checkModifiers_concreteMethod() throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod("three", Number.class);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                MathematicalMethodUtil.checkModifiers(method));

        assertEquals("The method nl.smith.mathematics.mathematicalfunctions.definition.AbstractExampleRecursiveFunctionContainer.three should be public and abstract.", exception.getMessage());
    }

    @Test
    void checkReturnType_nullArgument() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> MathematicalMethodUtil.checkReturnType(null));

        assertEquals("Please specify a method.", exception.getMessage());
    }

    @Test
    void checkReturnType_notGenericReturnType() throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod("four", BigDecimal.class);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> MathematicalMethodUtil.checkReturnType(method));

        assertEquals(format("The return type of AbstractExampleRecursiveFunctionContainer.four is not valid.%n" +
                "It should be: T or T[] with <T extends Number>"), exception.getMessage());
    }

    @Test
    void checkReturnType() throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod("five", Number.class);

        MathematicalMethodUtil.checkReturnType(method);
    }
}

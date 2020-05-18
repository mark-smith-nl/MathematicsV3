package nl.smith.mathematics.mathematicalfunctions.implementation;

import nl.smith.mathematics.mathematicalfunctions.MathematicalMethod;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainerExample;
import nl.smith.mathematics.mathematicalfunctions.definition.AbstractExampleRecursiveFunctionContainer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BigDecimalExampleRecursiveFunctionContainerTest extends RecursiveFunctionContainerExample<BigDecimalExampleRecursiveFunctionContainer> {

    @Test
    void getNumberType() {
        assertEquals(BigDecimal.class, functionContainer().getNumberType());
    }

    @Test
    void getName() {
        assertEquals("Name AbstractExampleRecursiveFunctionContainer", functionContainer().getName());
    }

    @Test
    void getDescription() {
        assertEquals("Description AbstractExampleRecursiveFunctionContainer", functionContainer().getDescription());
    }

    @Test
    void getMathematicalFunctions() throws NoSuchMethodException {
        Map<String, MathematicalMethod> mathematicalFunctions = functionContainer().getMathematicalFunctions();
        assertEquals(2, mathematicalFunctions.size());

        assertTrue(mathematicalFunctions.containsKey("five"));
        MathematicalMethod mathematicalMethod = mathematicalFunctions.get("five");
        assertEquals("five", mathematicalMethod.getName());
        assertEquals("function: Five", mathematicalMethod.getDescription());
        assertEquals("public abstract Number AbstractExampleRecursiveFunctionContainer.five(Number)", mathematicalMethod.getSignature());
        assertEquals(AbstractExampleRecursiveFunctionContainer.class.getDeclaredMethod("five", new Class<?>[]{Number.class}), mathematicalMethod.getMethod());

        assertTrue(mathematicalFunctions.containsKey("Method 6"));
        mathematicalMethod = mathematicalFunctions.get("Method 6");
        assertEquals("Method 6", mathematicalMethod.getName());
        assertEquals("function: Six", mathematicalMethod.getDescription());
        assertEquals("public abstract Number AbstractExampleRecursiveFunctionContainer.six(Number[])", mathematicalMethod.getSignature());

        assertEquals(AbstractExampleRecursiveFunctionContainer.class.getDeclaredMethod("six", new Class<?>[]{Number[].class}), mathematicalMethod.getMethod());
    }

}

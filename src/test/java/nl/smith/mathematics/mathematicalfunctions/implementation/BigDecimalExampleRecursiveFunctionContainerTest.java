package nl.smith.mathematics.mathematicalfunctions.implementation;

import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainerExample;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        }

}

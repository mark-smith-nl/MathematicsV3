package nl.smith.mathematics.domain;

import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MathematicalFunctionMethodMappingTest {


    @Test
    @MethodSource("constructor_usingNullArguments")
    public void constructor_usingNullArguments() {
        IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> new MathematicalFunctionMethodMapping<RationalNumber>(null, null));
        assertEquals("Please specify a container and a method.", actualException.getMessage());
    }


}
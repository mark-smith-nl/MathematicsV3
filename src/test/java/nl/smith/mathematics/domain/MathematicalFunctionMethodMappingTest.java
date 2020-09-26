package nl.smith.mathematics.domain;

import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MathematicalFunctionMethodMappingTest {

    @Test
    public void constructor_usingNullArguments() {
        IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> new MathematicalFunctionMethodMapping<RationalNumber>(null, null));
        assertEquals("Please specify a container and a method.", actualException.getMessage());
    }


}
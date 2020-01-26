package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalWellFormedFunctions;
import org.junit.jupiter.api.Test;

/** Test class to test for {@link FunctionContainer}. */
public class FunctionContainerTest {

    @Test
    public void getNumberType() {

        BigDecimalWellFormedFunctions bigDecimalWellFormedFunctions = new BigDecimalWellFormedFunctions();

        bigDecimalWellFormedFunctions.initializeNumberType();

        System.out.println(bigDecimalWellFormedFunctions.getNumberType());
    }
}

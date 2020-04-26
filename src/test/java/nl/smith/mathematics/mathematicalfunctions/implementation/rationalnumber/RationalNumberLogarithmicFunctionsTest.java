package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.configuration.constant.rationalnumber.Euler;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainerTest;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Stream;

import static nl.smith.mathematics.numbertype.RationalNumber.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RationalNumberLogarithmicFunctionsTest extends FunctionContainerTest<RationalNumberLogarithmicFunctions> {

    @DisplayName("Testing ln using @MethodSource('lnArguments\')")
    @ParameterizedTest
    @MethodSource("lnArguments")
    void ln(RationalNumber argument, RationalNumber expectedResult, String expectedConstraintMessage) {
        if (expectedConstraintMessage != null) {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> functionContainer().ln(argument));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        } else {

            RationalNumber functionValue = functionContainer().ln(argument);

            RationalNumber error;
            if (expectedResult.equals(ZERO)) {
                if (functionValue.equals(ZERO)) {
                    error = ZERO;
                } else {
                    error = functionValue.subtract(functionValue).divide(functionValue).multiply(100).abs();
                }
            } else {
                error = functionValue.subtract(expectedResult).divide(expectedResult).multiply(100).abs();
            }
            assertEquals(-1, error.compareTo(RationalNumber.valueOf("1E[-12]")));
        }
    }

    private static Stream<Arguments> lnArguments() {
        return Stream.of(
                Arguments.of(null, null, "No value specified"),
                Arguments.of(new RationalNumber(-1), null, "Value -1/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (-1/1) > 0 is not true"),
                Arguments.of(new RationalNumber(0), null, "Value 0/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (0/1) > 0 is not true"),
                Arguments.of(new RationalNumber(1, 2), RationalNumber.valueOf("-0.693147180559945"), null),
                Arguments.of(new RationalNumber(1), ZERO, null),
                Arguments.of(new RationalNumber(10), RationalNumber.valueOf("2.302585092994046"), null),
                Arguments.of(new RationalNumber(20), RationalNumber.valueOf("2.995732273553991"), null),
                Arguments.of(new RationalNumber(40), RationalNumber.valueOf("3.688879454113936"), null),
                Arguments.of(new RationalNumber(1000), RationalNumber.valueOf("6.907755278982137"), null),
                Arguments.of(Euler.get(), RationalNumber.valueOf("1.00000000000001"), null)
        );
    }
}
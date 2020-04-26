package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.mathematicalfunctions.FunctionContainerTest;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RationalNumberAuxiliaryFunctionsTest extends FunctionContainerTest<RationalNumberAuxiliaryFunctions> {

    @DisplayName("Testing faculty using null argument")
    @ParameterizedTest
    @NullSource
    void faculty_usingNullArgument(RationalNumber argument) {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> functionContainer.faculty(argument));
        Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
        assertEquals("No value specified", constraintViolation.getMessage());
    }

    @DisplayName("Testing faculty using @MethodSource(\"facultyArguments\"")
    @ParameterizedTest
    @MethodSource("facultyArguments")
    void faculty(RationalNumber argument, RationalNumber result, String expectedConstraintMessage) {
        if (expectedConstraintMessage != null) {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> functionContainer.faculty(argument));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        } else {
            assertEquals(result, functionContainer.faculty(argument));
        }
    }

    private static Stream<Arguments> facultyArguments() {
        return Stream.of(
                Arguments.of(new RationalNumber(0), new RationalNumber(1), null),
                Arguments.of(new RationalNumber(1), new RationalNumber(1), null),
                Arguments.of(new RationalNumber(2), new RationalNumber(2), null),
                Arguments.of(new RationalNumber(3), new RationalNumber(6), null),
                Arguments.of(new RationalNumber(4), new RationalNumber(24), null),
                Arguments.of(new RationalNumber(10), new RationalNumber(3628800), null),
                Arguments.of(new RationalNumber(20), new RationalNumber(2432902008176640000l), null),
                Arguments.of(new RationalNumber(101), RationalNumber.valueOf("2432902008176640000"), "Value 101/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 <= (101/1) <= 100 is not true")
        );
    }
}
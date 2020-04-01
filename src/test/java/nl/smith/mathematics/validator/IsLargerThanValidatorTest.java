package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Tests the {@link nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan} constraint used on a validated service method argument */
@SpringBootTest
class IsLargerThanValidatorTest {

    private final MethodContainer methodContainer;

    @Autowired
    public IsLargerThanValidatorTest(MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @DisplayName("Checking if a number service method argument is larger than a specified value")
    @ParameterizedTest
    @MethodSource({"numbers"})
    void isLargerThan(Object number, ConstraintViolationException expectedConstraintException) {
        if (expectedConstraintException != null) {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingLargerThan(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintException.getMessage(), constraintViolation.getMessage());
        } else {
            methodContainer.validatedMethodUsingLargerThan(number);
        }
    }

    //TODO Repair test
    @DisplayName("Checking if a number service method argument is larger than or equal to a specified value")
    @ParameterizedTest
    @MethodSource({"numbers"})
    void isLargerThanOrEqualsTo(Object number, ConstraintViolationException expectedConstraintException) {
        if (expectedConstraintException != null) {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingLargerThanOrEqualsTo(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintException.getMessage(), constraintViolation.getMessage());
        } else {
            methodContainer.validatedMethodUsingLargerThanOrEqualsTo(number);
        }
    }

    private static Stream<Arguments> numbers() {
        return Stream.of(
                Arguments.of("44", new ConstraintViolationException("Value 44(java.lang.String) is not a number or the assumption 4 < (44) is not true", null)),
                Arguments.of(new BigInteger("4"), new ConstraintViolationException("Value 4(java.math.BigInteger) is not a number or the assumption 4 < (4) is not true", null)),
                Arguments.of(new BigDecimal("4"), new ConstraintViolationException("Value 4(java.math.BigDecimal) is not a number or the assumption 4 < (4) is not true", null)),
                Arguments.of(RationalNumber.valueOf("4"), new ConstraintViolationException("Value 4/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 4 < (4/1) is not true", null)),
                Arguments.of(new BigInteger("3"), new ConstraintViolationException("Value 3(java.math.BigInteger) is not a number or the assumption 4 < (3) is not true", null)),
                Arguments.of(new BigDecimal("3"), new ConstraintViolationException("Value 3(java.math.BigDecimal) is not a number or the assumption 4 < (3) is not true", null)),
                Arguments.of(RationalNumber.valueOf("3"), new ConstraintViolationException("Value 3/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 4 < (3/1) is not true", null)),
                Arguments.of(new BigInteger("-3"), new ConstraintViolationException("Value -3(java.math.BigInteger) is not a number or the assumption 4 < (-3) is not true", null)),
                Arguments.of(new BigDecimal("-3"), new ConstraintViolationException("Value -3(java.math.BigDecimal) is not a number or the assumption 4 < (-3) is not true", null)),
                Arguments.of(RationalNumber.valueOf("-3"), new ConstraintViolationException("Value -3/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 4 < (-3/1) is not true", null)),
                Arguments.of(new BigInteger("44"), null),
                Arguments.of(new BigDecimal("44"), null),
                Arguments.of(RationalNumber.valueOf("44"), null)
        );
    }

    @Service
    @Validated
    public static class MethodContainer {

        public void validatedMethodUsingLargerThan(@IsLargerThan(floor = "4") Object argument) {}

        public void validatedMethodUsingLargerThanOrEqualsTo(@IsLargerThan(floor = "4", includingFloor = true) Object argument) {}

    }
}
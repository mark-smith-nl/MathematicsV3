package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @DisplayName("Checking if a validated number service method argument is larger than a specified value")
    @ParameterizedTest
    @MethodSource({"numbers_isLargerThan"})
    void isLargerThan(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingLargerThanAnnotation(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingLargerThanAnnotation(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        } }

    @DisplayName("Checking if a validated number service method argument is larger than or equal to a specified value")
    @ParameterizedTest
    @MethodSource({"numbers_isLargerThanOrEqualsTo"})
    void isLargerThanOrEqualsTo(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingLargerThanOrEqualsToAnnotation(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingLargerThanOrEqualsToAnnotation(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        }
    }

    @Test
    void doIt() {
        methodContainer.validatedMethodUsingLargerThanAnnotationOnPrimitiveParameter(5);
    }

    private static Stream<Arguments> numbers_isLargerThan() {
        return Stream.of(
                Arguments.of("44", "Value 44(java.lang.String) is not a number or the assumption (44) > 4 is not true"),

                Arguments.of(new BigInteger("4"), "Value 4(java.math.BigInteger) is not a number or the assumption (4) > 4 is not true"),
                Arguments.of(new BigDecimal("4"), "Value 4(java.math.BigDecimal) is not a number or the assumption (4) > 4 is not true"),
                Arguments.of(RationalNumber.valueOf("4"), "Value 4/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (4/1) > 4 is not true"),

                Arguments.of(new BigInteger("3"), "Value 3(java.math.BigInteger) is not a number or the assumption (3) > 4 is not true"),
                Arguments.of(new BigDecimal("3"), "Value 3(java.math.BigDecimal) is not a number or the assumption (3) > 4 is not true"),
                Arguments.of(RationalNumber.valueOf("3"), "Value 3/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (3/1) > 4 is not true"),

                Arguments.of(new BigInteger("-3"), "Value -3(java.math.BigInteger) is not a number or the assumption (-3) > 4 is not true"),
                Arguments.of(new BigDecimal("-3"), "Value -3(java.math.BigDecimal) is not a number or the assumption (-3) > 4 is not true"),
                Arguments.of(RationalNumber.valueOf("-3"), "Value -3/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (-3/1) > 4 is not true"),

                Arguments.of(new BigInteger("44"), null),
                Arguments.of(new BigDecimal("44"), null),
                Arguments.of(RationalNumber.valueOf("44"), null),

                Arguments.of(null, null)
        );
    }

    private static Stream<Arguments> numbers_isLargerThanOrEqualsTo() {
        return Stream.of(
                Arguments.of("44", "Value 44(java.lang.String) is not a number or the assumption (44) >= 4 is not true"),

                Arguments.of(new BigInteger("4"), null),
                Arguments.of(new BigDecimal("4"), null),
                Arguments.of(RationalNumber.valueOf("4"), null),

                Arguments.of(new BigInteger("3"), "Value 3(java.math.BigInteger) is not a number or the assumption (3) >= 4 is not true"),
                Arguments.of(new BigDecimal("3"), "Value 3(java.math.BigDecimal) is not a number or the assumption (3) >= 4 is not true"),
                Arguments.of(RationalNumber.valueOf("3"), "Value 3/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (3/1) >= 4 is not true"),

                Arguments.of(new BigInteger("-3"), "Value -3(java.math.BigInteger) is not a number or the assumption (-3) >= 4 is not true"),
                Arguments.of(new BigDecimal("-3"), "Value -3(java.math.BigDecimal) is not a number or the assumption (-3) >= 4 is not true"),
                Arguments.of(RationalNumber.valueOf("-3"), "Value -3/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (-3/1) >= 4 is not true"),

                Arguments.of(new BigInteger("44"), null),
                Arguments.of(new BigDecimal("44"), null),
                Arguments.of(RationalNumber.valueOf("44"), null),

                Arguments.of(null, null)
        );
    }

    @Service
    @Validated
    public static class MethodContainer {

        public void validatedMethodUsingLargerThanAnnotation(@IsLargerThan("4") Object argument) {}

        public void validatedMethodUsingLargerThanAnnotationOnPrimitiveParameter(@IsLargerThan("4") int argument) {}

        public void validatedMethodUsingLargerThanOrEqualsToAnnotation(@IsLargerThan(value = "4", includingBoundary = true) Object argument) {}

    }
}
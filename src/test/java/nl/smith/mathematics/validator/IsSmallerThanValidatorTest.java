package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsSmallerThan;
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

/** Tests the {@link nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsSmallerThan} constraint used on a validated service method argument */
@SpringBootTest
class IsSmallerThanValidatorTest {

    private final MethodContainer methodContainer;

    @Autowired
    public IsSmallerThanValidatorTest(MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @DisplayName("Checking if a validated number service method argument is smaller than a specified value")
    @ParameterizedTest
    @MethodSource({"numbers_isSmallerThan"})
    void isSmallerThan(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingSmallerThanAnnotation(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingSmallerThanAnnotation(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        }
    }

    @DisplayName("Checking if a validated number service method argument is smaller than or equal to a specified value")
    @ParameterizedTest
    @MethodSource({"numbers_isSmallerThanOrEqualsTo"})
    void isSmallerThanOrEqualsTo(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingSmallerThanOrEqualsToAnnotation(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingSmallerThanOrEqualsToAnnotation(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        }
    }

    private static Stream<Arguments> numbers_isSmallerThan() {
        return Stream.of(
                Arguments.of("44", "Value 44(java.lang.String) is not a number or the assumption (44) < 4 is not true"),
                Arguments.of(new BigInteger("4"), "Value 4(java.math.BigInteger) is not a number or the assumption (4) < 4 is not true"),
                Arguments.of(new BigDecimal("4"), "Value 4(java.math.BigDecimal) is not a number or the assumption (4) < 4 is not true"),
                Arguments.of(RationalNumber.valueOf("4"), "Value 4/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (4/1) < 4 is not true"),
                Arguments.of(new BigInteger("3"), null),
                Arguments.of(new BigDecimal("3"), null),
                Arguments.of(RationalNumber.valueOf("3"), null),
                Arguments.of(new BigInteger("-3"), null),
                Arguments.of(new BigDecimal("-3"), null),
                Arguments.of(RationalNumber.valueOf("-3"), null),
                Arguments.of(new BigInteger("44"), "Value 44(java.math.BigInteger) is not a number or the assumption (44) < 4 is not true"),
                Arguments.of(new BigDecimal("44"), "Value 44(java.math.BigDecimal) is not a number or the assumption (44) < 4 is not true"),
                Arguments.of(RationalNumber.valueOf("44"), "Value 44/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (44/1) < 4 is not true")
        );
    }

    private static Stream<Arguments> numbers_isSmallerThanOrEqualsTo() {
        return Stream.of(
                Arguments.of("44", "Value 44(java.lang.String) is not a number or the assumption (44) <= 4 is not true"),

                Arguments.of(new BigInteger("4"), null),
                Arguments.of(new BigDecimal("4"), null),
                Arguments.of(RationalNumber.valueOf("4"), null),

                Arguments.of(new BigInteger("3"), null),
                Arguments.of(new BigDecimal("3"), null),
                Arguments.of(RationalNumber.valueOf("3"), null),

                Arguments.of(new BigInteger("-3"), null),
                Arguments.of(new BigDecimal("-3"), null),
                Arguments.of(RationalNumber.valueOf("-3"), null),

                Arguments.of(new BigInteger("44"), "Value 44(java.math.BigInteger) is not a number or the assumption (44) <= 4 is not true"),
                Arguments.of(new BigDecimal("44"), "Value 44(java.math.BigDecimal) is not a number or the assumption (44) <= 4 is not true"),
                Arguments.of(RationalNumber.valueOf("44"), "Value 44/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (44/1) <= 4 is not true")
        );
    }

    @Service
    @Validated
    public static class MethodContainer {

        public void validatedMethodUsingSmallerThanAnnotation(@IsSmallerThan("4") Object argument) {
        }

        public void validatedMethodUsingSmallerThanOrEqualsToAnnotation(@IsSmallerThan(value = "4", includingBoundary = true) Object argument) {
        }

    }

}
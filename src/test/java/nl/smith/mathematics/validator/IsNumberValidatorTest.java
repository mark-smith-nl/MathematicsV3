package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsBetween;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNumber;
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
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/** Tests the {@link nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNumber} constraint used on a validated service method argument */
@SpringBootTest
class IsNumberValidatorTest {

    private final MethodContainer methodContainer;

    @Autowired
    public IsNumberValidatorTest(MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @DisplayName("Checking if a method's argument is a number")
    @ParameterizedTest
    @MethodSource({"arguments"})
    void isNumber(Object argument, String expectedConstraintMessage) {
        if (expectedConstraintMessage != null) {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsNumberAnnotation(argument));
            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        } else {
            methodContainer.validatedMethodUsingIsNumberAnnotation(argument);
        }
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("44", "Value(java.lang.String) 44 is not a number"),

                Arguments.of(new BigInteger("100"), null),
                Arguments.of(new BigDecimal("100"), null),
                Arguments.of(new RationalNumber(100), null),

                Arguments.of(null, null)
        );
    }
    @Service
    @Validated
    public static class MethodContainer {

        public void validatedMethodUsingIsNumberAnnotation(@IsNumber Object argument) {}

    }
}
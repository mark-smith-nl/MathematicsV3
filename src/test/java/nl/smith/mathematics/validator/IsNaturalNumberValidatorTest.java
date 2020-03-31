package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.util.RationalNumberUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IsNaturalNumberValidatorTest {

    private final MethodContainer methodContainer;

    @Autowired
    public IsNaturalNumberValidatorTest(MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @DisplayName("Checking if annotated parameter is null")
    @ParameterizedTest
    @NullSource
    void isNaturalNumber_nullArgument(Object number) {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethod(null));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
        assertEquals("No argument specified", constraintViolation.getMessage());
    }

    @DisplayName("Checking if annotated parameter is not a number type")
    @Test
    void isNaturalNumber_stringArgument() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethod("a string"));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
        assertEquals("Value is not an integer number: 'a string'", constraintViolation.getMessage());
    }

    @DisplayName("Checking if rational number is a natural number")
    @ParameterizedTest
    @MethodSource({"naturalNumbers"})
    void isNaturalNumber_BigIntegerArgument(Object naturalNumber) {
        methodContainer.validatedMethod(naturalNumber);
    }

    @DisplayName("Checking if number is not a natural number")
    @ParameterizedTest
    @MethodSource({"notNaturalNumbers"})
    void isNaturalNumber_BigDecimalArgument_notANaturalNumber(Object notNaturalNumber, String expectedMessage) {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethod(notNaturalNumber));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
        assertEquals(expectedMessage, constraintViolation.getMessage());
    }

    private static Stream<Arguments> naturalNumbers() {
        return Stream.of(
                Arguments.of(new BigInteger("44")),
                Arguments.of(new BigInteger("-44")),
                Arguments.of(new BigDecimal("44.0")),
                Arguments.of(new BigDecimal("-44.0")),
                Arguments.of(new RationalNumber(44, 1)),
                Arguments.of(new RationalNumber(-44, 1))
        );
    }

    private static Stream<Arguments> notNaturalNumbers() {
        return Stream.of(
                Arguments.of(new BigDecimal("44.5"), "Value is not an integer number: '44.5'"),
                Arguments.of(new RationalNumber(445, 10), "Value is not an integer number: '445/10'")
        );
    }

    @Service
    @Validated
    public static class MethodContainer {

        public void validatedMethod(@NotNull(message = "No argument specified") @IsNaturalNumber Object argument) {}
   }
}
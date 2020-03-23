package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.util.RationalNumberUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IsNaturalNumberValidatorTest {

    private final MethodContainer methodContainer;

    @Autowired
    public IsNaturalNumberValidatorTest(MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @Test
    void isNaturalNumber_nullArgument() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethod(null));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
        assertEquals("No argument specified", constraintViolation.getMessage());
    }

    @Test
    void isNaturalNumber_stringArgument() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethod("a string"));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
        assertEquals("Value is not an integer number: 'a string'", constraintViolation.getMessage());
    }

    @Test
    void isNaturalNumber_BigIntegerArgument() {
        methodContainer.validatedMethod(new BigInteger("44"));
    }

    @Test
    void isNaturalNumber_BigDecimalArgument() {
        methodContainer.validatedMethod(new BigDecimal("44.0"));
    }


    @Test
    void isNaturalNumber_RationalNumberArgument() {
        methodContainer.validatedMethod(new RationalNumber(44, 1));
    }

    @Test
    void isNaturalNumber_BigDecimalArgument_notANaturalNumber() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethod(new BigDecimal("44.5")));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
        assertEquals("Value is not an integer number: '44.5'", constraintViolation.getMessage());
    }

    @Test
    void isNaturalNumber_RationalNumberArgument_notANaturalNumber() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethod(new RationalNumber(445, 10)));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
        assertEquals("Value is not an integer number: '445/10'", constraintViolation.getMessage());
    }

    @Service
    @Validated
    public static class MethodContainer {

        public void validatedMethod(@NotNull(message = "No argument specified") @IsNaturalNumber Object argument) {}
   }
}
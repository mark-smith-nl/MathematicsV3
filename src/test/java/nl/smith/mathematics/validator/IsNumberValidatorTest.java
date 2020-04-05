package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNumber;
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

    @DisplayName("Checking if a method's primitive argument is a number using primitive number types")
    @Test
    void isNumberUsingPrimitiveUsingPrimitiveNumberTypes() {
        methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveIntArgument(4);
        methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveLongArgument(4l);
        methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveDoubleArgument(4.3);
        methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveFloatArgument(4.3f);
     }

    @DisplayName("Checking if a method's primitive argument is a number using primitive non number types")
    @Test
    void isNumberUsingPrimitiveUsingPrimitiveNonNumberTypes() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveBooleanArgument(true));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
        assertEquals("Value(java.lang.Boolean) true is not a number", constraintViolation.getMessage());

        exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveCharArgument('a'));
        constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        constraintViolation = constraintViolations.stream().findFirst().get();
        assertEquals("Value(java.lang.Character) a is not a number", constraintViolation.getMessage());
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("44", "Value(java.lang.String) 44 is not a number"),

                Arguments.of(Integer.valueOf(100), null),
                Arguments.of(Long.valueOf(100), null),
                Arguments.of(Double.valueOf(100.1), null),
                Arguments.of(Float.valueOf("100.1"), null),
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

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveIntArgument(@IsNumber int argument) {}

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveLongArgument(@IsNumber long argument) {}

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveDoubleArgument(@IsNumber double argument) {}

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveFloatArgument(@IsNumber float argument) {}

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveBooleanArgument(@IsNumber boolean argument) {}

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveCharArgument(@IsNumber char argument) {}

    }
}
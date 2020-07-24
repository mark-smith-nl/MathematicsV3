package nl.smith.mathematics.validator.mathematicalfunctionargument;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNumber;
import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNumber} constraint used on a validated service method argument
 */
@SpringBootTest
class IsNumberValidatorTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(IsNumberValidatorTest.class);

    private final MethodContainer methodContainer;

    @Autowired
    public IsNumberValidatorTest(MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @BeforeEach
    public void init() {
        RationalNumberOutputType.Type outputType = RationalNumberOutputType.Type.COMPONENTS;
        LOGGER.info("Setting rational number output type to {} ({})", outputType.name(), outputType.getDescription());
        RationalNumberOutputType.set(outputType);
    }

    @DisplayName("Checking if a method's argument is a number")
    @ParameterizedTest
    @MethodSource({"arguments"})
    void isNumber(Object argument, String expectedConstraintMessage) {
        if (expectedConstraintMessage != null) {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsNumberAnnotation(argument));
            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
            assertTrue(constraintViolationOption.isPresent());
            ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        } else {
            methodContainer.validatedMethodUsingIsNumberAnnotation(argument);
        }
    }

    @DisplayName("Checking if a method's primitive argument is a number using primitive number types")
    @Test
    void isNumberUsingPrimitiveUsingPrimitiveNumberTypes() {
        methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveIntArgument(4);
        methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveLongArgument(4L);
        methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveDoubleArgument(4.3);
        methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveFloatArgument(4.3f);
    }

    @DisplayName("Checking if a method's primitive argument is a number using primitive non number types")
    @Test
    void isNumberUsingPrimitiveUsingPrimitiveNonNumberTypes() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveBooleanArgument(true));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
        assertTrue(constraintViolationOption.isPresent());
        ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
        assertEquals("Value(java.lang.Boolean) true is not a number", constraintViolation.getMessage());

        exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsNumberAnnotationWithPrimitiveCharArgument('a'));
        constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        constraintViolationOption = constraintViolations.stream().findFirst();
        assertTrue(constraintViolationOption.isPresent());
        constraintViolation = constraintViolationOption.get();
        assertEquals("Value(java.lang.Character) a is not a number", constraintViolation.getMessage());
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("44", "Value(java.lang.String) 44 is not a number"),

                Arguments.of(100, null),
                Arguments.of(100L, null),
                Arguments.of(100.1, null),
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

        public void validatedMethodUsingIsNumberAnnotation(@IsNumber Object argument) {
            LOGGER.debug("Calling method validatedMethodUsingIsNumberAnnotation");}

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveIntArgument(@IsNumber int argument) {
            LOGGER.debug("Calling method validatedMethodUsingIsNumberAnnotationWithPrimitiveIntArgument");}

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveLongArgument(@IsNumber long argument) {
            LOGGER.debug("Calling method validatedMethodUsingIsNumberAnnotationWithPrimitiveLongArgument");}

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveDoubleArgument(@IsNumber double argument) {
            LOGGER.debug("Calling method validatedMethodUsingIsNumberAnnotationWithPrimitiveDoubleArgument");}

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveFloatArgument(@IsNumber float argument) {
            LOGGER.debug("Calling method validatedMethodUsingIsNumberAnnotationWithPrimitiveFloatArgument");}

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveBooleanArgument(@IsNumber boolean argument) {
            LOGGER.debug("Calling method validatedMethodUsingIsNumberAnnotationWithPrimitiveBooleanArgument");}

        public void validatedMethodUsingIsNumberAnnotationWithPrimitiveCharArgument(@IsNumber char argument) {
            LOGGER.debug("Calling method validatedMethodUsingIsNumberAnnotationWithPrimitiveCharArgument");}

    }
}
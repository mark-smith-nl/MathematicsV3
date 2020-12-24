package nl.smith.mathematics.validator.mathematicalfunctionargument;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberOutputType;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IsNaturalNumberValidatorTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(IsNaturalNumberValidatorTest.class);

    private final MethodContainer methodContainer;

    @Autowired
    public IsNaturalNumberValidatorTest(MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @BeforeEach
    public void init() {
        RationalNumberOutputType.PredefinedType outputPredefinedType = RationalNumberOutputType.PredefinedType.COMPONENTS;
        LOGGER.info("Setting rational number output type to {} ({})", outputPredefinedType.name(), outputPredefinedType.valueDescription());
        RationalNumberOutputType.value().set(outputPredefinedType);
    }

    @DisplayName("Checking if annotated parameter is null")
    @ParameterizedTest
    @NullSource
    void isNaturalNumber_nullArgument(Object number) {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingNotNullAndIsNaturalNumberAnnotation(null));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
        assertTrue(constraintViolationOption.isPresent());
        ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
        assertEquals("No argument specified", constraintViolation.getMessage());
    }

    @DisplayName("Checking if annotated parameter is not a number type")
    @Test
    void isNaturalNumber_stringArgument() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingNotNullAndIsNaturalNumberAnnotation("a string"));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
        assertTrue(constraintViolationOption.isPresent());
        ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
        assertEquals("Value is not a natural number: 'a string'", constraintViolation.getMessage());
    }

    @DisplayName("Checking if rational number is a natural number")
    @ParameterizedTest
    @MethodSource({"naturalNumbers"})
    void isNaturalNumber_BigIntegerArgument(Object naturalNumber) {
        methodContainer.validatedMethodUsingNotNullAndIsNaturalNumberAnnotation(naturalNumber);
    }

    @DisplayName("Checking if number is not a natural number")
    @ParameterizedTest
    @MethodSource({"notNaturalNumbers"})
    void isNaturalNumber_BigDecimalArgument_notANaturalNumber(Object notNaturalNumber, String expectedConstraintMessage) {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingNotNullAndIsNaturalNumberAnnotation(notNaturalNumber));
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
        assertTrue(constraintViolationOption.isPresent());
        ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
        assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
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
                Arguments.of(new BigDecimal("44.5"), "Value is not a natural number: '44.5'"),
                Arguments.of(new RationalNumber(445, 10), "Value is not a natural number: '445/10'") // Note: Rational numbers are normalized.
        );
    }

    @Service
    @Validated
    public static class MethodContainer {

        public void validatedMethodUsingNotNullAndIsNaturalNumberAnnotation(@NotNull(message = "No argument specified") @IsNaturalNumber Object argument) {
            LOGGER.debug("Calling method validatedMethodUsingNotNullAndIsNaturalNumberAnnotation");
        }
   }
}

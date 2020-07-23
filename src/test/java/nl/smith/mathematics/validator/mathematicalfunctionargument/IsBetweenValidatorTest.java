package nl.smith.mathematics.validator.mathematicalfunctionargument;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsBetween;
import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
 * Tests the {@link nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsBetween} constraint used on a validated service method argument
 */
@SpringBootTest
class IsBetweenValidatorTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(IsBetweenValidatorTest.class);

    private final MethodContainer methodContainer;

    @Autowired
    public IsBetweenValidatorTest(MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @BeforeEach
    public void init() {
        RationalNumberOutputType.Type outputType = RationalNumberOutputType.Type.COMPONENTS;
        LOGGER.info("Setting rational number output type to {} ({})", outputType.name(), outputType.getDescription());
        RationalNumberOutputType.set(outputType);
    }

    @DisplayName("Checking if a validated number service method argument is between two a specified values including boundaries")
    @ParameterizedTest
    @MethodSource({"numbers_isBetweenIncludingBoundaries"})
    void isBetweenIncludingBoundaries(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingIsBetweenAnnotationIncludingBoundaries(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsBetweenAnnotationIncludingBoundaries(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
            assertTrue(constraintViolationOption.isPresent());
            ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        }
    }

    @DisplayName("Checking if a validated number service method argument is between two a specified values excluding boundaries")
    @ParameterizedTest
    @MethodSource({"numbers_isBetweenExcludingBoundaries"})
    void isBetweenExcludingBoundaries(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingIsBetweenAnnotationExcludingBoundaries(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsBetweenAnnotationExcludingBoundaries(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
            assertTrue(constraintViolationOption.isPresent());
            ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        }
    }

    @DisplayName("Checking if a validated number service method argument is between two a specified values including lower boundary")
    @ParameterizedTest
    @MethodSource({"numbers_isBetweenIncludingLowerBoundary"})
    void isBetweenIncludingLowerBoundary(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingIsBetweenAnnotationIncludingLowerBoundary(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsBetweenAnnotationIncludingLowerBoundary(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
            assertTrue(constraintViolationOption.isPresent());
            ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        }
    }

    @DisplayName("Checking if a validated number service method argument is between two a specified values including upper boundary")
    @ParameterizedTest
    @MethodSource({"numbers_isBetweenIncludingUpperBoundary"})
    void isBetweenIncludingUpperBoundary(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {methodContainer.validatedMethodUsingIsBetweenAnnotationIncludingUpperBoundary(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsBetweenAnnotationIncludingUpperBoundary(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
            assertTrue(constraintViolationOption.isPresent());
            ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        }
    }

    private static Stream<Arguments> numbers_isBetweenIncludingBoundaries() {
        return Stream.of(
                Arguments.of("144", "Value 144(java.lang.String) is not a number or the assumption 0 <= (144) <= 100 is not true"),

                Arguments.of(new BigInteger("144"), "Value 144(java.math.BigInteger) is not a number or the assumption 0 <= (144) <= 100 is not true"),
                Arguments.of(new BigDecimal("144"), "Value 144(java.math.BigDecimal) is not a number or the assumption 0 <= (144) <= 100 is not true"),
                Arguments.of(new RationalNumber(144), "Value 144/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 <= (144/1) <= 100 is not true"),

                Arguments.of(new BigInteger("-1"), "Value -1(java.math.BigInteger) is not a number or the assumption 0 <= (-1) <= 100 is not true"),
                Arguments.of(new BigDecimal("-1"), "Value -1(java.math.BigDecimal) is not a number or the assumption 0 <= (-1) <= 100 is not true"),
                Arguments.of(new RationalNumber(-1), "Value -1/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 <= (-1/1) <= 100 is not true"),

                Arguments.of(new BigInteger("0"), null),
                Arguments.of(new BigDecimal("0"), null),
                Arguments.of(new RationalNumber(0), null),

                Arguments.of(new BigInteger("4"), null),
                Arguments.of(new BigDecimal("4"), null),
                Arguments.of(new RationalNumber(4), null),

                Arguments.of(new BigInteger("100"), null),
                Arguments.of(new BigDecimal("100"), null),
                Arguments.of(new RationalNumber(100), null),

                Arguments.of(null, null)
        );
    }

    private static Stream<Arguments> numbers_isBetweenExcludingBoundaries() {
        return Stream.of(
                Arguments.of("144", "Value 144(java.lang.String) is not a number or the assumption 0 < (144) < 100 is not true"),

                Arguments.of(new BigInteger("144"), "Value 144(java.math.BigInteger) is not a number or the assumption 0 < (144) < 100 is not true"),
                Arguments.of(new BigDecimal("144"), "Value 144(java.math.BigDecimal) is not a number or the assumption 0 < (144) < 100 is not true"),
                Arguments.of(new RationalNumber(144), "Value 144/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 < (144/1) < 100 is not true"),

                Arguments.of(new BigInteger("-1"), "Value -1(java.math.BigInteger) is not a number or the assumption 0 < (-1) < 100 is not true"),
                Arguments.of(new BigDecimal("-1"), "Value -1(java.math.BigDecimal) is not a number or the assumption 0 < (-1) < 100 is not true"),
                Arguments.of(new RationalNumber(-1), "Value -1/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 < (-1/1) < 100 is not true"),

                Arguments.of(new BigInteger("0"), "Value 0(java.math.BigInteger) is not a number or the assumption 0 < (0) < 100 is not true"),
                Arguments.of(new BigDecimal("0"), "Value 0(java.math.BigDecimal) is not a number or the assumption 0 < (0) < 100 is not true"),
                Arguments.of(new RationalNumber(0), "Value 0/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 < (0/1) < 100 is not true"),

                Arguments.of(new BigInteger("4"), null),
                Arguments.of(new BigDecimal("4"), null),
                Arguments.of(new RationalNumber(4), null),

                Arguments.of(new BigInteger("100"), "Value 100(java.math.BigInteger) is not a number or the assumption 0 < (100) < 100 is not true"),
                Arguments.of(new BigDecimal("100"), "Value 100(java.math.BigDecimal) is not a number or the assumption 0 < (100) < 100 is not true"),
                Arguments.of(new RationalNumber(100), "Value 100/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 < (100/1) < 100 is not true"),

                Arguments.of(null, null)
        );
    }

    private static Stream<Arguments> numbers_isBetweenIncludingLowerBoundary() {
        return Stream.of(
                Arguments.of("144", "Value 144(java.lang.String) is not a number or the assumption 0 <= (144) < 100 is not true"),

                Arguments.of(new BigInteger("144"), "Value 144(java.math.BigInteger) is not a number or the assumption 0 <= (144) < 100 is not true"),
                Arguments.of(new BigDecimal("144"), "Value 144(java.math.BigDecimal) is not a number or the assumption 0 <= (144) < 100 is not true"),
                Arguments.of(new RationalNumber(144), "Value 144/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 <= (144/1) < 100 is not true"),

                Arguments.of(new BigInteger("-1"), "Value -1(java.math.BigInteger) is not a number or the assumption 0 <= (-1) < 100 is not true"),
                Arguments.of(new BigDecimal("-1"), "Value -1(java.math.BigDecimal) is not a number or the assumption 0 <= (-1) < 100 is not true"),
                Arguments.of(new RationalNumber(-1), "Value -1/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 <= (-1/1) < 100 is not true"),

                Arguments.of(new BigInteger("0"), null),
                Arguments.of(new BigDecimal("0"), null),
                Arguments.of(new RationalNumber(0), null),

                Arguments.of(new BigInteger("4"), null),
                Arguments.of(new BigDecimal("4"), null),
                Arguments.of(new RationalNumber(4), null),

                Arguments.of(new BigInteger("100"), "Value 100(java.math.BigInteger) is not a number or the assumption 0 <= (100) < 100 is not true"),
                Arguments.of(new BigDecimal("100"), "Value 100(java.math.BigDecimal) is not a number or the assumption 0 <= (100) < 100 is not true"),
                Arguments.of(new RationalNumber(100), "Value 100/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 <= (100/1) < 100 is not true"),

                Arguments.of(null, null)
        );
    }

    private static Stream<Arguments> numbers_isBetweenIncludingUpperBoundary() {
        return Stream.of(
                Arguments.of("144", "Value 144(java.lang.String) is not a number or the assumption 0 < (144) <= 100 is not true"),

                Arguments.of(new BigInteger("144"), "Value 144(java.math.BigInteger) is not a number or the assumption 0 < (144) <= 100 is not true"),
                Arguments.of(new BigDecimal("144"), "Value 144(java.math.BigDecimal) is not a number or the assumption 0 < (144) <= 100 is not true"),
                Arguments.of(new RationalNumber(144), "Value 144/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 < (144/1) <= 100 is not true"),

                Arguments.of(new BigInteger("-1"), "Value -1(java.math.BigInteger) is not a number or the assumption 0 < (-1) <= 100 is not true"),
                Arguments.of(new BigDecimal("-1"), "Value -1(java.math.BigDecimal) is not a number or the assumption 0 < (-1) <= 100 is not true"),
                Arguments.of(new RationalNumber(-1), "Value -1/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 < (-1/1) <= 100 is not true"),

                Arguments.of(new BigInteger("0"), "Value 0(java.math.BigInteger) is not a number or the assumption 0 < (0) <= 100 is not true"),
                Arguments.of(new BigDecimal("0"), "Value 0(java.math.BigDecimal) is not a number or the assumption 0 < (0) <= 100 is not true"),
                Arguments.of(new RationalNumber(0), "Value 0/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 < (0/1) <= 100 is not true"),

                Arguments.of(new BigInteger("4"), null),
                Arguments.of(new BigDecimal("4"), null),
                Arguments.of(new RationalNumber(4), null),

                Arguments.of(new BigInteger("100"), null),
                Arguments.of(new BigDecimal("100"), null),
                Arguments.of(new RationalNumber(100), null),

                Arguments.of(null, null)
        );
    }

    @Service
    @Validated
    public static class MethodContainer {

        public void validatedMethodUsingIsBetweenAnnotationIncludingBoundaries(@IsBetween(floor = "0", includingFloor = true, ceiling = "100", includingCeiling = true) Object argument) {
        }

        public void validatedMethodUsingIsBetweenAnnotationExcludingBoundaries(@IsBetween(floor = "0", ceiling = "100") Object argument) {
        }

        public void validatedMethodUsingIsBetweenAnnotationIncludingLowerBoundary(@IsBetween(floor = "0", includingFloor = true, ceiling = "100") Object argument) {
        }

        public void validatedMethodUsingIsBetweenAnnotationIncludingUpperBoundary(@IsBetween(floor = "0", ceiling = "100", includingCeiling = true) Object argument) {
        }
    }
}
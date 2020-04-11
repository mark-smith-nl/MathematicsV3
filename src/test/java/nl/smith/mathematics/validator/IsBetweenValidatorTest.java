package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsBetween;
import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.util.UserSystemContext;
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
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        RationalNumber.OutputType outputType = RationalNumber.OutputType.COMPONENTS;
        LOGGER.info("Setting rational number output type to {} ({})", outputType.name(), outputType.getDescription());
        UserSystemContext.setValue("outputType", outputType);
    }

    @DisplayName("Checking if a validated number service method argument is between two a specified values including boundaries")
    @ParameterizedTest
    @MethodSource({"numbers_isBetweenIncludingBoundaries"})
    void isBetweenIncludingBoundaries(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingisBetweenAnnotationIncludingBoundaries(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingisBetweenAnnotationIncludingBoundaries(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        }
    }

    @DisplayName("Checking if a validated number service method argument is between two a specified values excluding boundaries")
    @ParameterizedTest
    @MethodSource({"numbers_isBetweenExcludingBoundaries"})
    void isBetweenExcludingBoundaries(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingisBetweenAnnotationExcludingBoundaries(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingisBetweenAnnotationExcludingBoundaries(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        }
    }

    @DisplayName("Checking if a validated number service method argument is between two a specified values including lower boundary")
    @ParameterizedTest
    @MethodSource({"numbers_isBetweenIncludingLowerBoundary"})
    void isBetweenIncludingLowerBoundary(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingisBetweenAnnotationIncludingLowerBoundary(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingisBetweenAnnotationIncludingLowerBoundary(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        }
    }

    @DisplayName("Checking if a validated number service method argument is between two a specified values including upper boundary")
    @ParameterizedTest
    @MethodSource({"numbers_isBetweenIncludingUpperBoundary"})
    void isBetweenIncludingUpperBoundary(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {methodContainer.validatedMethodUsingisBetweenAnnotationIncludingUpperBoundary(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingisBetweenAnnotationIncludingUpperBoundary(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
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

        public void validatedMethodUsingisBetweenAnnotationIncludingBoundaries(@IsBetween(floor = "0", includingFloor = true, ceiling = "100", includingCeiling = true) Object argument) {
        }

        public void validatedMethodUsingisBetweenAnnotationExcludingBoundaries(@IsBetween(floor = "0", ceiling = "100") Object argument) {
        }

        public void validatedMethodUsingisBetweenAnnotationIncludingLowerBoundary(@IsBetween(floor = "0", includingFloor = true, ceiling = "100") Object argument) {
        }

        public void validatedMethodUsingisBetweenAnnotationIncludingUpperBoundary(@IsBetween(floor = "0", ceiling = "100", includingCeiling = true) Object argument) {
        }
    }
}
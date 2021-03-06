package nl.smith.mathematics.validator.mathematicalfunctionargument;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberOutputType;
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

/** Tests the {@link nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan} constraint used on a validated service method argument */
@SpringBootTest
class IsLargerThanValidatorTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(IsLargerThanValidatorTest.class);

    private final MethodContainer methodContainer;

    @Autowired
    public IsLargerThanValidatorTest(MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @BeforeEach
    public void init() {
        RationalNumberOutputType.PredefinedType outputPredefinedType = RationalNumberOutputType.PredefinedType.COMPONENTS;
        LOGGER.info("Setting rational number output type to {} ({})", outputPredefinedType.name(), outputPredefinedType.valueDescription());
        RationalNumberOutputType.value().set(outputPredefinedType);
    }

    @DisplayName("Checking if a validated number service method argument is larger than a specified value")
    @ParameterizedTest
    @MethodSource({"numbers_isLargerThan"})
    void isLargerThan(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingIsLargerThanAnnotation(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsLargerThanAnnotation(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
            assertTrue(constraintViolationOption.isPresent());
            ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        } }

    @DisplayName("Checking if a validated number service method argument is larger than or equal to a specified value")
    @ParameterizedTest
    @MethodSource({"numbers_isLargerThanOrEqualsTo"})
    void isLargerThanOrEqualsTo(Object number, String expectedConstraintMessage) {
        if (expectedConstraintMessage == null) {
            methodContainer.validatedMethodUsingIsLargerThanOrEqualsToAnnotation(number);
        } else {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingIsLargerThanOrEqualsToAnnotation(number));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
            assertTrue(constraintViolationOption.isPresent());
            ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        }
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

        public void validatedMethodUsingIsLargerThanAnnotation(@IsLargerThan("4") Object argument) {
            LOGGER.debug("Calling method validatedMethodUsingIsLargerThanAnnotation");
        }

        public void validatedMethodUsingIsLargerThanOrEqualsToAnnotation(@IsLargerThan(value = "4", includingBoundary = true) Object argument) {
            LOGGER.debug("Calling method validatedMethodUsingIsLargerThanOrEqualsToAnnotation");
        }

    }
}

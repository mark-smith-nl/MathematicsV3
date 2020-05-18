package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainerExample;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Stream;

import static nl.smith.mathematics.configuration.constant.NumberConstant.rationalValueOf;
import static nl.smith.mathematics.numbertype.RationalNumber.ONE;
import static nl.smith.mathematics.numbertype.RationalNumber.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class RationalNumberLogarithmicFunctionsExample extends RecursiveFunctionContainerExample<RationalNumberLogarithmicFunctions> {

    @Test
    void getNumberType() {
        assertEquals(RationalNumber.class, functionContainer().getNumberType());
    }

    @ParameterizedTest
    @MethodSource("exp")
    void exp(RationalNumber argument, RationalNumber expectedResult, String expectedConstraintMessage) {
        if (expectedConstraintMessage != null) {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> functionContainer().ln(argument));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        } else {
            RationalNumber functionValue = functionContainer().exp(argument);

            RationalNumber error;
            if (expectedResult.equals(ZERO)) {
                if (functionValue.equals(ZERO)) {
                    error = ZERO;
                } else {
                    error = functionValue.subtract(functionValue).divide(functionValue).multiply(100).abs();
                }
            } else {
                error = functionValue.subtract(expectedResult).divide(expectedResult).multiply(100).abs();
            }
            assertEquals(-1, error.compareTo(rationalValueOf.MaximumError.get()));
        }
    }


    @ParameterizedTest
    @MethodSource("ln")
    @Timeout(20)
    void ln(RationalNumber argument, RationalNumber expectedResult, String expectedConstraintMessage) {
        if (expectedConstraintMessage != null) {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> functionContainer().ln(argument));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
            assertEquals(expectedConstraintMessage, constraintViolation.getMessage());
        } else {
            //TaylorDegreeOfPolynom.set(100);
            RationalNumber functionValue = functionContainer().ln(argument);

            RationalNumber error;
            if (expectedResult.equals(ZERO)) {
                if (functionValue.equals(ZERO)) {
                    error = ZERO;
                } else {
                    error = functionValue.subtract(functionValue).divide(functionValue).multiply(100).abs();
                }
            } else {
                error = functionValue.subtract(expectedResult).divide(expectedResult).multiply(100).abs();
            }
             assertEquals(-1, error.compareTo(rationalValueOf.MaximumError.get()));
        }
    }

    private static Stream<Arguments> exp() {
        return Stream.of(
                Arguments.of(null, null, "No value specified"),
                Arguments.of(new RationalNumber(-100), RationalNumber.valueOf("3.720075976020836E[-44]"), null),
                Arguments.of(new RationalNumber(-50), RationalNumber.valueOf("1.928749847963918E[-22]"), null),
                Arguments.of(new RationalNumber(-25), RationalNumber.valueOf("0.000000000013888"), null),
                Arguments.of(new RationalNumber(-10), RationalNumber.valueOf("0.000045399929762"), null),
                Arguments.of(new RationalNumber(-5), RationalNumber.valueOf("0.006737946999085"), null),
                Arguments.of(new RationalNumber(-1), RationalNumber.valueOf("0.367879441171442"), null),
                Arguments.of(new RationalNumber(0), ONE, null),
                Arguments.of(new RationalNumber(1), RationalNumber.valueOf("2.718281828459045"), null),
                Arguments.of(new RationalNumber(5), RationalNumber.valueOf("148.413159102576603"), null),
                Arguments.of(new RationalNumber(10), RationalNumber.valueOf("22026.465794806716517"), null),
                Arguments.of(new RationalNumber(25), RationalNumber.valueOf("72004899337.385872524161351"), null),
                Arguments.of(new RationalNumber(100), RationalNumber.valueOf("2.688117141816136E[43]"), null)
        );
    }

    private static Stream<Arguments> ln() {
        return Stream.of(
             //   Arguments.of(null, null, "No value specified"),
             //   Arguments.of(new RationalNumber(-1), null, "Value -1/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (-1/1) > 0 is not true"),
              //  Arguments.of(new RationalNumber(0), null, "Value 0/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption (0/1) > 0 is not true"),
            //    Arguments.of(new RationalNumber(1, 2), RationalNumber.valueOf("-0.693147180559945"), null),
             //   Arguments.of(new RationalNumber(1, 3), RationalNumber.valueOf("-1.09861228866811"), null)
                //Arguments.of(new RationalNumber(1, 4), RationalNumber.valueOf("-0.693147180559945"), null),
             //   Arguments.of(new RationalNumber(1), ZERO, null),
              //  Arguments.of(new RationalNumber(1), ZERO, null),
                Arguments.of(RationalNumber.valueOf("2.718281828459045"), ONE, null)
              //  Arguments.of(new RationalNumber(25), RationalNumber.valueOf("3.218875824868201"), null)
             //   Arguments.of(new RationalNumber(50), RationalNumber.valueOf("3.912023005428146"), null)
        //        Arguments.of(new RationalNumber(1000), RationalNumber.valueOf("6.907755278982137"), null),
        //        Arguments.of(Euler.get(), RationalNumber.valueOf("1.00000000000001"), null),
        //        Arguments.of(RationalNumber.valueOf("0.0000453999366246206300148931558949734423593056112"), ONE, null)
        );
    }

}
package nl.smith.mathematics.service;

import nl.smith.mathematics.mathematicalfunctions.MathematicalFunctionMethodMapping;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MethodRunnerServiceTest {

    private final MethodRunnerService methodRunnerService;

    @Autowired
    public MethodRunnerServiceTest(MethodRunnerService methodRunnerService) {
        this.methodRunnerService = methodRunnerService;
    }

    @Test
    public void getNumberTypes() {
        assertEquals(new HashSet<>(Arrays.asList(BigDecimal.class, RationalNumber.class)), methodRunnerService.getNumberTypes());
    }

    @Test
    public void getAvailableFunctionContainersForNumberType() {
        Set<String> siblingBeanNames = methodRunnerService.getAvailableFunctionContainersForNumberType(BigDecimal.class)
                .stream().map(RecursiveValidatedService::getSiblingBeanName).collect(Collectors.toSet());
        assertTrue(siblingBeanNames.containsAll(Arrays.asList(
                "BIG_DECIMAL_ARITHMETIC_FUNCTIONS",
                "BIG_DECIMAL_AUXILIARY_FUNCTIONS",
                "BIG_DECIMAL_GONIOMETRIC_FUNCTIONS",
                "BIG_DECIMAL_LOGARITHMIC_FUNCTIONS",
                "BIG_DECIMAL_STATISTICAL_FUNCTIONS")));


        siblingBeanNames = methodRunnerService.getAvailableFunctionContainersForNumberType(RationalNumber.class)
                .stream().map(RecursiveValidatedService::getSiblingBeanName).collect(Collectors.toSet());
        assertTrue(siblingBeanNames.containsAll(Arrays.asList(
                "RATIONAL_NUMBER_ARITHMETIC_FUNCTIONS",
                "RATIONAL_NUMBER_AUXILIARY_FUNCTIONS",
                "RATIONAL_NUMBER_GONIOMETRIC_FUNCTIONS",
                "RATIONAL_NUMBER_LOGARITHMIC_FUNCTIONS",
                "RATIONAL_NUMBER_STATISTICAL_FUNCTIONS")));
    }

    @Test
    public void getRecursiveFunctionContainers() {
        Set<String> siblingBeanNames = methodRunnerService.getRecursiveFunctionContainers().stream().map(RecursiveValidatedService::getSiblingBeanName).collect(Collectors.toSet());

        assertTrue(siblingBeanNames.containsAll(Arrays.asList(
                "BIG_DECIMAL_ARITHMETIC_FUNCTIONS",
                "BIG_DECIMAL_AUXILIARY_FUNCTIONS",
                "BIG_DECIMAL_GONIOMETRIC_FUNCTIONS",
                "BIG_DECIMAL_LOGARITHMIC_FUNCTIONS",
                "BIG_DECIMAL_STATISTICAL_FUNCTIONS")));

        assertTrue(siblingBeanNames.containsAll(Arrays.asList(
                "RATIONAL_NUMBER_ARITHMETIC_FUNCTIONS",
                "RATIONAL_NUMBER_AUXILIARY_FUNCTIONS",
                "RATIONAL_NUMBER_GONIOMETRIC_FUNCTIONS",
                "RATIONAL_NUMBER_LOGARITHMIC_FUNCTIONS",
                "RATIONAL_NUMBER_STATISTICAL_FUNCTIONS")));
    }

    @ParameterizedTest
    @MethodSource("getMathematicalMethodsForNumberType")
    public void getMathematicalMethodsForNumberType(String description) {

        Set<String> descriptions = methodRunnerService.getMathematicalFunctionsForNumberType(BigDecimal.class).stream().map(MathematicalFunctionMethodMapping::toString).collect(Collectors.toSet());

        assertTrue(descriptions.contains(description));

        descriptions = methodRunnerService.getMathematicalFunctionsForNumberType(RationalNumber.class).stream().map(MathematicalFunctionMethodMapping::toString).collect(Collectors.toSet());

        assertTrue(descriptions.contains(description));
    }

    @ParameterizedTest
    @MethodSource("getBinaryArithmeticMethodsForNumberType")
    public void getBinaryArithmeticMethodsForNumberType(String description) {
        Set<String> descriptions = methodRunnerService.getBinaryArithmeticMethodsForNumberType(BigDecimal.class).stream().map(MathematicalFunctionMethodMapping::toString).collect(Collectors.toSet());

        assertTrue(descriptions.contains(description));

        descriptions = methodRunnerService.getBinaryArithmeticMethodsForNumberType(RationalNumber.class).stream().map(MathematicalFunctionMethodMapping::toString).collect(Collectors.toSet());

        assertTrue(descriptions.contains(description));
    }

    @ParameterizedTest
    @MethodSource("getUnaryArithmeticMethodsForNumberType")
    public void getUnaryArithmeticMethodsForNumberType(String description) {
        Set<String> descriptions = methodRunnerService.getUnaryArithmeticMethodsForNumberType(BigDecimal.class).stream().map(MathematicalFunctionMethodMapping::toString).collect(Collectors.toSet());

        assertTrue(descriptions.contains(description));

        descriptions = methodRunnerService.getUnaryArithmeticMethodsForNumberType(RationalNumber.class).stream().map(MathematicalFunctionMethodMapping::toString).collect(Collectors.toSet());

        assertTrue(descriptions.contains(description));
    }

    @ParameterizedTest
    @MethodSource("getArithmeticMethodsForNumberType")
    public void getArithmeticMethodsForNumberType(String description) {
        Set<String> descriptions = methodRunnerService.getArithmeticMethodsForNumberType(BigDecimal.class).stream().map(MathematicalFunctionMethodMapping::toString).collect(Collectors.toSet());

        assertTrue(descriptions.contains(description));

        descriptions = methodRunnerService.getArithmeticMethodsForNumberType(RationalNumber.class).stream().map(MathematicalFunctionMethodMapping::toString).collect(Collectors.toSet());

        assertTrue(descriptions.contains(description));
    }

    @ParameterizedTest
    @MethodSource("invokeMathematicalMethodForNumberType")
    public void invokeMathematicalMethodForNumberType(Class<? extends Number> numberType, String functionName, Number expectedValue, Exception expectedException, Number[] arguments) {
        if (expectedException == null) {
            if (numberType == RationalNumber.class){
                RationalNumber actualValue = methodRunnerService.invokeMathematicalMethodForNumberType(numberType, functionName, (RationalNumber[]) arguments);
                assertEquals(actualValue, expectedValue);
            }
        }


      //  assertEquals(new RationalNumber(120), faculty);
    }

    private static Stream<Arguments> getMathematicalMethodsForNumberType() {
        return Stream.of(
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.StatisticalFunctions.prod                           ---> prod(N[])           FUNCTION                           (Product of a set of numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.StatisticalFunctions.deviation                      ---> deviation(N[])      FUNCTION                           (Standard deviation of a set of numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions.tan                            ---> tan(N)              FUNCTION                           (The tangent of an angle)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions.sin                            ---> sin(N)              FUNCTION                           (The sinus of an angle)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.LogarithmicFunctions.exp                            ---> exp(N)              FUNCTION                           (The exp of a number)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.StatisticalFunctions.square                         ---> square(N[])         FUNCTION                           (SQUARE)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.AuxiliaryFunctions.faculty                          ---> faculty(N)          FUNCTION                           (Faculty of a number)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.LogarithmicFunctions.power                          ---> power(N, N)         FUNCTION                           (The number raised to the power)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.StatisticalFunctions.sum                            ---> sum(N[])            FUNCTION                           (Sum of a set of numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.LogarithmicFunctions.ln                             ---> ln(N)               FUNCTION                           (The natural logarithm of a number)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions.cos                            ---> cos(N)              FUNCTION                           (The cosines of an angle)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.StatisticalFunctions.keyNumber                      ---> keyNumber(N, N[])   FUNCTION                           (Standard deviation of a set of numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.LogarithmicFunctions.sqrt                           ---> sqrt(N)             FUNCTION                           (The square root of a number)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.StatisticalFunctions.average                        ---> average(N[])        FUNCTION                           (Average of a set of numbers)")
        );
    }

    private static Stream<Arguments> getBinaryArithmeticMethodsForNumberType() {
        return Stream.of(
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.plus                            ---> +(N, N)             BINARY_OPERATION                   (Sum of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.divideBy                        ---> /(N, N)             BINARY_OPERATION_HIGH_PRIORITY     (Division of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.minus                           ---> -(N, N)             BINARY_OPERATION                   (Subtraction of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.multiplyBy                      ---> *(N, N)             BINARY_OPERATION_HIGH_PRIORITY     (Multiplication of two numbers)")
        );
    }

    private static Stream<Arguments> getUnaryArithmeticMethodsForNumberType() {
        return Stream.of(
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.minus                           ---> -(N)                UNARY_OPERATION                    (Negate number)")
        );
    }

    private static Stream<Arguments> getArithmeticMethodsForNumberType() {
        return Stream.of(
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.plus                            ---> +(N, N)             BINARY_OPERATION                   (Sum of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.divideBy                        ---> /(N, N)             BINARY_OPERATION_HIGH_PRIORITY     (Division of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.minus                           ---> -(N, N)             BINARY_OPERATION                   (Subtraction of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.multiplyBy                      ---> *(N, N)             BINARY_OPERATION_HIGH_PRIORITY     (Multiplication of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.minus                           ---> -(N)                UNARY_OPERATION                    (Negate number)")
        );
    }

    private static Stream<Arguments> invokeMathematicalMethodForNumberType() {
        return Stream.of(
                Arguments.of(RationalNumber.class, "faculty", new RationalNumber(120), null, new RationalNumber[]{new RationalNumber(5)}),
          //TODO exception testing      Arguments.of(RationalNumber.class, "faculty", new RationalNumber(120), null, new RationalNumber[]{new RationalNumber(500)}),
                Arguments.of(RationalNumber.class, "+", new RationalNumber(5), null, new RationalNumber[]{new RationalNumber(2), new RationalNumber(3)}),
                Arguments.of(RationalNumber.class, "*", new RationalNumber(6), null, new RationalNumber[]{new RationalNumber(2), new RationalNumber(3)}),
                Arguments.of(RationalNumber.class, "/", new RationalNumber(2, 3), null, new RationalNumber[]{new RationalNumber(2), new RationalNumber(3)}),
                Arguments.of(RationalNumber.class, "*", new RationalNumber(2), null, new RationalNumber[]{RationalNumber.valueOf("0.[142857]R"), new RationalNumber(14)}),
                Arguments.of(RationalNumber.class, "-", new RationalNumber(-1, 7), null, new RationalNumber[]{RationalNumber.valueOf("0.[142857]R")})
        );

    }
}
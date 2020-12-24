package nl.smith.mathematics.service;

import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberOutputType;
import nl.smith.mathematics.domain.MathematicalFunctionMethodMapping;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MethodRunnerServiceTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(MethodRunnerServiceTest.class);

    private final MethodRunnerService methodRunnerService;

    @Autowired
    public MethodRunnerServiceTest(MethodRunnerService methodRunnerService) {
        this.methodRunnerService = methodRunnerService;
    }

    @BeforeEach
    public void init() {
        RationalNumberOutputType.PredefinedType outputPredefinedType = RationalNumberOutputType.PredefinedType.COMPONENTS;
        LOGGER.info("Setting rational number output type to {} ({})", outputPredefinedType.name(), outputPredefinedType.valueDescription());
        RationalNumberOutputType.value().set(outputPredefinedType);
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
    @MethodSource({"invokeMathematicalMethodWithIllegalArguments"})
    //@MethodSource({"invokeMathematicalMethodWithIllegalArguments", "invokeMathematicalMethodForRationalNumbers", "invokeMathematicalMethodForBigDecimals"})
    public void invokeMathematicalMethodForNumberType(Class<Number> numberType, String functionName, Number expectedValue, Exception expectedException, Number[] arguments) {
        if (expectedException == null) {
            Number actualValue = methodRunnerService.invokeMathematicalMethodForNumberType(numberType, functionName, arguments);
            assertEquals(actualValue, expectedValue);
        } else {
            Exception actualException = assertThrows(Exception.class, () -> methodRunnerService.invokeMathematicalMethodForNumberType(numberType, functionName, arguments));
            assertEquals(expectedException.getClass(), actualException.getClass());

            if (expectedException instanceof ConstraintViolationException) {
                Map<String, String> expected = ((ConstraintViolationException) expectedException).getConstraintViolations().stream().collect(Collectors.toMap(cv -> cv.getPropertyPath().toString(), ConstraintViolation::getMessage));
                Map<String, String> actual = ((ConstraintViolationException) actualException).getConstraintViolations().stream().collect(Collectors.toMap(cv -> cv.getPropertyPath().toString(), ConstraintViolation::getMessage));
                assertEquals(expected, actual);
            } else {

                assertEquals(expectedException.getMessage(), actualException.getMessage());
            }
        }
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
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.divideBy                        ---> /(N, N)             HIGH_PRIORITY_BINARY_OPERATION     (Division of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.minus                           ---> -(N, N)             BINARY_OPERATION                   (Subtraction of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.multiplyBy                      ---> *(N, N)             HIGH_PRIORITY_BINARY_OPERATION     (Multiplication of two numbers)")
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
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.divideBy                        ---> /(N, N)             HIGH_PRIORITY_BINARY_OPERATION     (Division of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.minus                           ---> -(N, N)             BINARY_OPERATION                   (Subtraction of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.multiplyBy                      ---> *(N, N)             HIGH_PRIORITY_BINARY_OPERATION     (Multiplication of two numbers)"),
                Arguments.of("nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions.minus                           ---> -(N)                UNARY_OPERATION                    (Negate number)")
        );
    }

    private static Stream<Arguments> invokeMathematicalMethodWithIllegalArguments() {
        Class<RationalNumber> numberType = RationalNumber.class;
        return Stream.of(
                Arguments.of(null,
                        "",
                        null,
                        new ConstraintViolationException("",
                                Stream.of(
                                        getConstraintViolation("invokeMathematicalMethodForNumberType.mathematicalMethodName", "must not be empty"),
                                        getConstraintViolation("invokeMathematicalMethodForNumberType.numberType", "Please specify a valid number type (class)."),
                                        getConstraintViolation("invokeMathematicalMethodForNumberType.arguments", "must not be empty"))
                                        .collect(Collectors.toSet())),
                        null),
                Arguments.of(RationalNumber.class, "",
                        null,
                        new ConstraintViolationException("",
                                Stream.of(
                                        getConstraintViolation("invokeMathematicalMethodForNumberType.mathematicalMethodName", "must not be empty"),
                                        getConstraintViolation("invokeMathematicalMethodForNumberType.arguments", "must not be empty"))
                                        .collect(Collectors.toSet())),
                        null),
                Arguments.of(RationalNumber.class,
                        "function",
                        null,
                        new ConstraintViolationException("",
                                Stream.of(
                                        getConstraintViolation("invokeMathematicalMethodForNumberType.arguments", "must not be empty"))
                                        .collect(Collectors.toSet())),
                        null),
                Arguments.of(RationalNumber.class,
                        "function",
                        null,
                        new ConstraintViolationException("",
                                Stream.of(
                                        getConstraintViolation("invokeMathematicalMethodForNumberType.arguments", "must not be empty"))
                                        .collect(Collectors.toSet())),
                        new RationalNumber[0])
        );
    }

    private static Stream<Arguments> invokeMathematicalMethodForRationalNumbers() {
        Class<RationalNumber> numberType = RationalNumber.class;
        return Stream.of(
                Arguments.of(numberType,
                        "faculty",
                        new RationalNumber(120),
                        null,
                        new RationalNumber[]{new RationalNumber(5)}),
                Arguments.of(numberType,
                        "faculty",
                        null,
                        new IllegalArgumentException("faculty.number: Value 500/1(nl.smith.mathematics.numbertype.RationalNumber) is not a number or the assumption 0 <= (500/1) <= 100 is not true"),
                        new RationalNumber[]{new RationalNumber(500)}),
                Arguments.of(numberType,
                        "faculty",
                        null,
                        new IllegalArgumentException("faculty.number: Value is not a natural number: '10/70'"),
                        new RationalNumber[]{new RationalNumber(10, 70)}),
                Arguments.of(numberType,
                        "sum",
                        new RationalNumber(45),
                        null,
                        new RationalNumber[]{new RationalNumber(1), new RationalNumber(2), new RationalNumber(3), new RationalNumber(4), new RationalNumber(5), new RationalNumber(6), new RationalNumber(7), new RationalNumber(8), new RationalNumber(9)}),
                Arguments.of(numberType,
                        "sumA",
                        null,
                        new IllegalStateException("Can not find a method sumA accepting 9 argument(s) of type nl.smith.mathematics.numbertype.RationalNumber.\nValue is null.\nExpected a nl.smith.mathematics.domain.MathematicalFunctionMethodMapping not null value."),
                        new RationalNumber[]{new RationalNumber(1), new RationalNumber(2), new RationalNumber(3), new RationalNumber(4), new RationalNumber(5), new RationalNumber(6), new RationalNumber(7), new RationalNumber(8), new RationalNumber(9)}),
                Arguments.of(numberType,
                        "+",
                        new RationalNumber(5),
                        null,
                        new RationalNumber[]{new RationalNumber(2), new RationalNumber(3)}),
                Arguments.of(numberType,
                        "*",
                        new RationalNumber(6),
                        null,
                        new RationalNumber[]{new RationalNumber(2), new RationalNumber(3)}),
                Arguments.of(numberType,
                        "/",
                        new RationalNumber(2, 3),
                        null,
                        new RationalNumber[]{new RationalNumber(2), new RationalNumber(3)}),
                Arguments.of(numberType,
                        "*",
                        new RationalNumber(2),
                        null,
                        new RationalNumber[]{RationalNumber.valueOf("0.[142857]R"),
                                new RationalNumber(14)}),
                Arguments.of(RationalNumber.class,
                        "-",
                        new RationalNumber(-1, 7),
                        null,
                        new RationalNumber[]{RationalNumber.valueOf("0.[142857]R")}),
                Arguments.of(BigDecimal.class,
                        "-",
                        null,
                        new IllegalArgumentException("Wrong type of number class.\nThe number type of the nl.smith.mathematics.service.MethodRunnerService instance is set to java.math.BigDecimal while the arguments for the method invocation are of type nl.smith.mathematics.numbertype.RationalNumber.\nBoth types should be equal."),
                        new RationalNumber[]{RationalNumber.valueOf("0.[142857]R")})
        );
    }

    private static Stream<Arguments> invokeMathematicalMethodForBigDecimals() {
        Class<BigDecimal> numberType = BigDecimal.class;
        return Stream.of(
                Arguments.of(numberType, "faculty",
                        new BigDecimal(120),
                        null,
                        new BigDecimal[]{new BigDecimal(5)}),
                Arguments.of(numberType,
                        "faculty",
                        null,
                        new IllegalArgumentException("faculty.number: Value 500(java.math.BigDecimal) is not a number or the assumption 0 <= (500) <= 100 is not true"),
                        new BigDecimal[]{new BigDecimal(500)}),
                Arguments.of(numberType,
                        "faculty",
                        null,
                        new IllegalArgumentException("faculty.number: Value is not a natural number: '0.142857'"),
                        new BigDecimal[]{new BigDecimal("0.142857")}),
                Arguments.of(numberType,
                        "sum",
                        new BigDecimal(45),
                        null,
                        new BigDecimal[]{new BigDecimal(1), new BigDecimal(2), new BigDecimal(3), new BigDecimal(4), new BigDecimal(5), new BigDecimal(6), new BigDecimal(7), new BigDecimal(8), new BigDecimal(9)}),
                Arguments.of(numberType,
                        "sumA",
                        null,
                        new IllegalStateException("Can not find a method sumA accepting 9 argument(s) of type java.math.BigDecimal.\nValue is null.\nExpected a nl.smith.mathematics.domain.MathematicalFunctionMethodMapping not null value."),
                        new BigDecimal[]{new BigDecimal(1), new BigDecimal(2), new BigDecimal(3), new BigDecimal(4), new BigDecimal(5), new BigDecimal(6), new BigDecimal(7), new BigDecimal(8), new BigDecimal(9)}),
                Arguments.of(numberType,
                        "+",
                        new BigDecimal(5),
                        null,
                        new BigDecimal[]{new BigDecimal(2), new BigDecimal(3)}),
                Arguments.of(numberType,
                        "*",
                        new BigDecimal(6),
                        null,
                        new BigDecimal[]{new BigDecimal(2), new BigDecimal(3)}),
                Arguments.of(numberType,
                        "/",
                        // See scale and rounding mode properties in the application.properties file
                        new BigDecimal("0.666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666667"),
                        null,
                        new BigDecimal[]{new BigDecimal(2), new BigDecimal(3)}),
                Arguments.of(numberType,
                        "*",
                        new BigDecimal("1.999998"),
                        null,
                        new BigDecimal[]{new BigDecimal("0.142857"),
                                new BigDecimal(14)}),
                Arguments.of(numberType,
                        "-",
                        new BigDecimal("-0.142857"),
                        null,
                        new BigDecimal[]{new BigDecimal("0.142857")}),
                Arguments.of(RationalNumber.class,
                        "-",
                        new BigDecimal("0.142857"),
                        new IllegalArgumentException("Wrong type of number class.\nThe number type of the nl.smith.mathematics.service.MethodRunnerService instance is set to nl.smith.mathematics.numbertype.RationalNumber while the arguments for the method invocation are of type java.math.BigDecimal.\nBoth types should be equal."),
                        new BigDecimal[]{new BigDecimal("0.142857")})
        );
    }

    private static ConstraintViolation<?> getConstraintViolation(String propertyPathAsString, String message) {
        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        Path propertyPath = mock(Path.class);
        when(constraintViolation.getPropertyPath()).thenReturn(propertyPath);
        when(constraintViolation.getMessage()).thenReturn(message);
        when(propertyPath.toString()).thenReturn(propertyPathAsString);

        return constraintViolation;
    }
}

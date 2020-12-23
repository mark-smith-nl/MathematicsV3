package nl.smith.mathematics.numbertype;

import nl.smith.mathematics.configuration.constant.RationalNumberNormalize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static nl.smith.mathematics.configuration.constant.RationalNumberNormalize.PredefinedType.NO;
import static nl.smith.mathematics.numbertype.RationalNumber.ONE;
import static nl.smith.mathematics.numbertype.RationalNumber.ZERO;
import static org.junit.jupiter.api.Assertions.*;

/**
 * System under test: {@link nl.smith.mathematics.numbertype.RationalNumber}
 */
public class RationalNumberTest {

    @Test
    void constructorUsingLong() {
        for (long i = -100; i <= 100; i++) {
            assertEquals(new RationalNumber(i).getNumerator(), BigInteger.valueOf(i));
        }
    }

    @Test
    void constructorUsingLongs() {
        for (long n = -10; n <= 10; n++) {
            for (int d = -10; d <= 10; d++) {
                long numerator = d < 0 ? -n : n;
                long denominator = d < 0 ? -d : d;

                if (denominator == 0) {
                    Exception exception = assertThrows(ArithmeticException.class, () -> new RationalNumber(numerator, denominator));
                    assertEquals("Division by zero", exception.getMessage());
                } else {

                    RationalNumber rationalNumber = new RationalNumber(numerator, denominator);
                    assertEquals(rationalNumber.getNumerator(), BigInteger.valueOf(numerator));
                    assertEquals(rationalNumber.getDenominator(), BigInteger.valueOf(denominator));
                }
            }
        }
    }

    @Test
    void constructorUsingBigInteger() {
        for (long numerator = -100; numerator <= 100; numerator++) {
            assertEquals(new RationalNumber(BigInteger.valueOf(numerator)).getNumerator(), BigInteger.valueOf(numerator));
        }
    }

    @ParameterizedTest
    @NullSource
    void constructorUsingNullBigInteger(BigInteger numerator) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new RationalNumber(numerator));
        assertEquals("Both numerator and denominator must be specified (not be null)", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("constructorUsingNullBigIntegers")
    void constructorUsingNullBigIntegers(BigInteger numerator, BigInteger denominator) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new RationalNumber(numerator, denominator));
        assertEquals("Both numerator and denominator must be specified (not be null)", exception.getMessage());
    }

    @Test
    void constructorUsingBigIntegers() {
        RationalNumberNormalize.set(NO);
        for (int n = -10; n <= 10; n++) {
            for (int d = -10; d <= 10; d++) {
                long numerator = d < 0 ? -n : n;
                long denominator = d < 0 ? -d : d;

                if (denominator == 0) {
                    Exception exception = assertThrows(ArithmeticException.class, () -> new RationalNumber(numerator, denominator));
                    assertEquals("Division by zero", exception.getMessage());
                } else {
                    RationalNumber rationalNumber = new RationalNumber(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
                    assertEquals(rationalNumber.getNumerator(), BigInteger.valueOf(numerator));
                    assertEquals(rationalNumber.getDenominator(), BigInteger.valueOf(denominator));
                }
            }
        }
    }

    @Test
    void constructorUsingBigIntegersDenominatorZero() {
        Exception exception = assertThrows(ArithmeticException.class, () -> new RationalNumber(BigInteger.valueOf(2), BigInteger.valueOf(0)));
        assertEquals("Division by zero", exception.getMessage());
    }

    @DisplayName("Testing the construction of a RationalNumber using its static public method valueOf()")
    @ParameterizedTest
    @MethodSource("numberString")
    void valueOf(String numberString) {
        assertNotNull(RationalNumber.valueOf(numberString));
    }

    @ParameterizedTest
    @MethodSource("getNormalizedComponents")
    void getNormalizedComponents(BigInteger numerator, BigInteger denominator, BigInteger expectedNumerator, BigInteger expectedDenominator, String expectedErrorMessage) {
        if (expectedErrorMessage != null) {
            ArithmeticException exception = assertThrows(ArithmeticException.class, () -> RationalNumber.getNormalizedComponents(numerator, denominator));
            assertEquals(expectedErrorMessage, exception.getMessage());
        } else {
            BigInteger[] normalizedComponents = RationalNumber.getNormalizedComponents(numerator, denominator);
            assertEquals(expectedNumerator, normalizedComponents[0]);
            assertEquals(expectedDenominator, normalizedComponents[1]);
        }
    }

    @ParameterizedTest
    @MethodSource("addUsingLong")
    void addUsingLong(RationalNumber rationalNumber, long augend, RationalNumber expectedResult) {
        RationalNumber result = rationalNumber.add(augend);

        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("addUsingBigInteger")
    void addUsingBigInteger(RationalNumber rationalNumber, BigInteger augend, RationalNumber expectedResult, String expectedErrorMessage) {
        if (expectedErrorMessage != null) {
            ArithmeticException exception = assertThrows(ArithmeticException.class, () -> rationalNumber.add(augend));
            assertEquals(expectedErrorMessage, exception.getMessage());
        } else {
            RationalNumber result = rationalNumber.add(augend);

            assertEquals(expectedResult, result);
        }
    }

    @ParameterizedTest
    @MethodSource("addUsingRationalNumber")
    void add(RationalNumber rationalNumber, RationalNumber augend, RationalNumber expectedResult, String expectedErrorMessage) {
        if (expectedErrorMessage != null) {
            ArithmeticException exception = assertThrows(ArithmeticException.class, () -> rationalNumber.add(augend));
            assertEquals(expectedErrorMessage, exception.getMessage());
        } else {
            RationalNumber result = rationalNumber.add(augend);

            assertEquals(expectedResult, result);
        }
    }

    @ParameterizedTest
    @MethodSource("subtractUsingLong")
    void subtractUsingLong(RationalNumber rationalNumber, long subtrahend, RationalNumber expectedResult) {
        RationalNumber result = rationalNumber.subtract(subtrahend);

        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("subtractUsingBigInteger")
    void subtractUsingBigInteger(RationalNumber rationalNumber, BigInteger subtrahend, RationalNumber expectedResult, String expectedErrorMessage) {
        if (expectedErrorMessage != null) {
            ArithmeticException exception = assertThrows(ArithmeticException.class, () -> rationalNumber.subtract(subtrahend));
            assertEquals(expectedErrorMessage, exception.getMessage());
        } else {
            RationalNumber result = rationalNumber.subtract(subtrahend);

            assertEquals(expectedResult, result);
        }
    }

    @ParameterizedTest
    @MethodSource("subtractUsingRationalNumber")
    void subtractUsingRationalNumber(RationalNumber rationalNumber, RationalNumber subtrahend, RationalNumber expectedResult, String expectedErrorMessage) {
        if (expectedErrorMessage != null) {
            ArithmeticException exception = assertThrows(ArithmeticException.class, () -> rationalNumber.subtract(subtrahend));
            assertEquals(expectedErrorMessage, exception.getMessage());
        } else {
            RationalNumber result = rationalNumber.subtract(subtrahend);

            assertEquals(expectedResult, result);
        }
    }

    @ParameterizedTest
    @MethodSource("multiplyUsingLong")
    void multiplyUsingLong(RationalNumber rationalNumber, long multiplicand, RationalNumber expectedResult) {
        RationalNumber result = rationalNumber.multiply(multiplicand);

        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("multiplyUsingBigInteger")
    void multiplyUsingBigInteger(RationalNumber rationalNumber, BigInteger multiplicand, RationalNumber expectedResult, String expectedErrorMessage) {
        if (expectedErrorMessage != null) {
            ArithmeticException exception = assertThrows(ArithmeticException.class, () -> rationalNumber.multiply(multiplicand));
            assertEquals(expectedErrorMessage, exception.getMessage());
        } else {
            RationalNumber result = rationalNumber.multiply(multiplicand);

            assertEquals(expectedResult, result);
        }
    }

    @ParameterizedTest
    @MethodSource("multiplyUsingRationalNumber")
    void multiplyUsingRationalNumber(RationalNumber rationalNumber, RationalNumber multiplicand, RationalNumber expectedResult, String expectedErrorMessage) {
        if (expectedErrorMessage != null) {
            ArithmeticException exception = assertThrows(ArithmeticException.class, () -> rationalNumber.multiply(multiplicand));
            assertEquals(expectedErrorMessage, exception.getMessage());
        } else {
            RationalNumber result = rationalNumber.multiply(multiplicand);

            assertEquals(expectedResult, result);
        }
    }

    @ParameterizedTest
    @MethodSource("divideUsingLong")
    void divideUsingLong(RationalNumber rationalNumber, long divisor, RationalNumber expectedResult, String expectedErrorMessage) {
        if (expectedErrorMessage != null) {
            ArithmeticException exception = assertThrows(ArithmeticException.class, () -> rationalNumber.divide(divisor));
            assertEquals(expectedErrorMessage, exception.getMessage());
        } else {
            RationalNumber result = rationalNumber.divide(divisor);

            assertEquals(expectedResult, result);
        }
    }

    @ParameterizedTest
    @MethodSource("divideUsingBigInteger")
    void divideUsingBigInteger(RationalNumber rationalNumber, BigInteger divisor, RationalNumber expectedResult, String expectedErrorMessage) {
        if (expectedErrorMessage != null) {
            ArithmeticException exception = assertThrows(ArithmeticException.class, () -> rationalNumber.divide(divisor));
            assertEquals(expectedErrorMessage, exception.getMessage());
        } else {
            RationalNumber result = rationalNumber.divide(divisor);

            assertEquals(expectedResult, result);
        }
    }

    @ParameterizedTest
    @MethodSource("divideUsingRationalNumber")
    void divideUsingRationalNumber(RationalNumber rationalNumber, RationalNumber divisor, RationalNumber expectedResult, String expectedErrorMessage) {
        if (expectedErrorMessage != null) {
            ArithmeticException exception = assertThrows(ArithmeticException.class, () -> rationalNumber.divide(divisor));
            assertEquals(expectedErrorMessage, exception.getMessage());
        } else {
            RationalNumber result = rationalNumber.divide(divisor);

            assertEquals(expectedResult, result);
        }
    }

    @ParameterizedTest
    @MethodSource("negate")
    void negate(RationalNumber rationalNumber, RationalNumber expectedResult) {
        RationalNumber result = rationalNumber.negate();

        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("abs")
    void abs(RationalNumber rationalNumber, RationalNumber expectedResult) {
        RationalNumber result = rationalNumber.abs();

        assertEquals(expectedResult, result);
    }

    @Test
    void equalsCompareWithNull() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);

        assertNotEquals(null, rationalNumber);
    }

    @Test
    void equalsCompareWithString() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);

        assertNotEquals("String", rationalNumber);
    }

    @Test
    void equalsCompare() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber otherRationalNumber = new RationalNumber(20, 30);

        assertEquals(rationalNumber, otherRationalNumber);
    }

    @ParameterizedTest
    @MethodSource("divideAndRemainder")
    void divideAndRemainder(RationalNumber number, RationalNumber divisor, RationalNumber integerValue, RationalNumber remainder) {
        RationalNumber[] divideAndRemainder = number.divideAndRemainder(divisor);
        assertEquals(integerValue, divideAndRemainder[0]);
        assertEquals(remainder, divideAndRemainder[1]);
    }

    private static Stream<Arguments> constructorUsingNullBigIntegers() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, BigInteger.valueOf(1)),
                Arguments.of(BigInteger.valueOf(1), null),
                Arguments.of(null, null)
        );
    }

    private static Stream<Arguments> getNormalizedComponents() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ONE, "BigInteger divide by zero"),
                Arguments.of(BigInteger.ZERO, BigInteger.ONE, BigInteger.ZERO, BigInteger.ONE, null),
                Arguments.of(BigInteger.ZERO, BigInteger.ONE.negate(), BigInteger.ZERO, BigInteger.ONE.negate(), null),
                Arguments.of(BigInteger.ZERO, BigInteger.TEN.negate(), BigInteger.ZERO, BigInteger.ONE.negate(), null),
                Arguments.of(BigInteger.ZERO, BigInteger.TEN, BigInteger.ZERO, BigInteger.ONE, null),
                Arguments.of(BigInteger.TEN, BigInteger.valueOf(5), BigInteger.valueOf(2), BigInteger.ONE, null),
                Arguments.of(BigInteger.TEN.negate(), BigInteger.valueOf(5), BigInteger.valueOf(2).negate(), BigInteger.ONE, null),
                Arguments.of(BigInteger.TEN, BigInteger.valueOf(5).negate(), BigInteger.valueOf(2), BigInteger.ONE.negate(), null),
                Arguments.of(BigInteger.TEN.negate(), BigInteger.valueOf(5).negate(), BigInteger.valueOf(2).negate(), BigInteger.ONE.negate(), null)
        );
    }

    private static Stream<Arguments> addUsingLong() {
        return Stream.of(
                Arguments.of(ZERO, 0L, ZERO),
                Arguments.of(ONE, 0L, ONE),
                Arguments.of(ZERO, 1L, ONE),
                Arguments.of(new RationalNumber(2), 3, new RationalNumber(5)),
                Arguments.of(new RationalNumber(3), 2, new RationalNumber(5)),
                Arguments.of(new RationalNumber(-2), 3, ONE),
                Arguments.of(new RationalNumber(3), -2, ONE),
                Arguments.of(new RationalNumber(-2), -3, new RationalNumber(-5)),
                Arguments.of(new RationalNumber(-3), -2, new RationalNumber(-5)),
                Arguments.of(new RationalNumber(4, 3), 1, new RationalNumber(7, 3), null)
        );
    }

    private static Stream<Arguments> addUsingBigInteger() {
        return Stream.of(
                Arguments.of(ONE, null, null, "Please specify an augend."),
                Arguments.of(ZERO, BigInteger.ZERO, ZERO, null),
                Arguments.of(ONE, BigInteger.ZERO, ONE, null),
                Arguments.of(ZERO, BigInteger.ONE, ONE, null),
                Arguments.of(new RationalNumber(2), BigInteger.valueOf(3), new RationalNumber(5), null),
                Arguments.of(new RationalNumber(3), BigInteger.valueOf(2), new RationalNumber(5), null),
                Arguments.of(new RationalNumber(-2), BigInteger.valueOf(3), ONE, null),
                Arguments.of(new RationalNumber(3), BigInteger.valueOf(-2), ONE, null),
                Arguments.of(new RationalNumber(-2), BigInteger.valueOf(-3), new RationalNumber(-5), null),
                Arguments.of(new RationalNumber(-3), BigInteger.valueOf(-2), new RationalNumber(-5), null),
                Arguments.of(new RationalNumber(4, 3), BigInteger.valueOf(1), new RationalNumber(7, 3), null)
        );
    }

    private static Stream<Arguments> addUsingRationalNumber() {
        return Stream.of(
                Arguments.of(ONE, null, null, "Please specify an augend."),
                Arguments.of(ZERO, ZERO, ZERO, null),
                Arguments.of(ONE, ZERO, ONE, null),
                Arguments.of(ZERO, ONE, ONE, null),
                Arguments.of(new RationalNumber(2), new RationalNumber(3), new RationalNumber(5), null),
                Arguments.of(new RationalNumber(3), new RationalNumber(2), new RationalNumber(5), null),
                Arguments.of(new RationalNumber(-2), new RationalNumber(3), ONE, null),
                Arguments.of(new RationalNumber(3), new RationalNumber(-2), ONE, null),
                Arguments.of(new RationalNumber(-2), new RationalNumber(-3), new RationalNumber(-5), null),
                Arguments.of(new RationalNumber(-3), new RationalNumber(-2), new RationalNumber(-5), null),
                Arguments.of(new RationalNumber(4, 3), new RationalNumber(1), new RationalNumber(7, 3), null)
        );
    }

    private static Stream<Arguments> subtractUsingLong() {
        return Stream.of(
                Arguments.of(ZERO, 0L, ZERO),
                Arguments.of(ONE, 0L, ONE),
                Arguments.of(ZERO, 1L, new RationalNumber(-1)),
                Arguments.of(new RationalNumber(2), 3, new RationalNumber(-1)),
                Arguments.of(new RationalNumber(3), 2, ONE),
                Arguments.of(new RationalNumber(-2), 3, new RationalNumber(-5)),
                Arguments.of(new RationalNumber(3), -2, new RationalNumber(5)),
                Arguments.of(new RationalNumber(-2), -3, ONE),
                Arguments.of(new RationalNumber(-3), -2, new RationalNumber(-1)),
                Arguments.of(new RationalNumber(4, 3), 1, new RationalNumber(1, 3), null)
        );
    }

    private static Stream<Arguments> subtractUsingBigInteger() {
        return Stream.of(
                Arguments.of(ZERO, null, ZERO, "Please specify a subtrahend."),
                Arguments.of(ZERO, BigInteger.ZERO, ZERO, null),
                Arguments.of(ONE, BigInteger.ZERO, ONE, null),
                Arguments.of(ZERO, BigInteger.valueOf(1), new RationalNumber(-1), null),
                Arguments.of(new RationalNumber(2), BigInteger.valueOf(3), new RationalNumber(-1), null),
                Arguments.of(new RationalNumber(3), BigInteger.valueOf(2), ONE, null),
                Arguments.of(new RationalNumber(-2), BigInteger.valueOf(3), new RationalNumber(-5), null),
                Arguments.of(new RationalNumber(3), BigInteger.valueOf(-2), new RationalNumber(5), null),
                Arguments.of(new RationalNumber(-2), BigInteger.valueOf(-3), ONE, null),
                Arguments.of(new RationalNumber(-3), BigInteger.valueOf(-2), new RationalNumber(-1), null),
                Arguments.of(new RationalNumber(4, 3), BigInteger.valueOf(1), new RationalNumber(1, 3), null),
                Arguments.of(new RationalNumber(4, 3), BigInteger.ONE, new RationalNumber(1, 3), null)
        );
    }

    private static Stream<Arguments> subtractUsingRationalNumber() {
        return Stream.of(
                Arguments.of(ZERO, null, ZERO, "Please specify a subtrahend."),
                Arguments.of(ZERO, ZERO, ZERO, null),
                Arguments.of(ONE, ZERO, ONE, null),
                Arguments.of(ZERO, new RationalNumber(1), new RationalNumber(-1), null),
                Arguments.of(new RationalNumber(2), new RationalNumber(3), new RationalNumber(-1), null),
                Arguments.of(new RationalNumber(3), new RationalNumber(2), ONE, null),
                Arguments.of(new RationalNumber(-2), new RationalNumber(3), new RationalNumber(-5), null),
                Arguments.of(new RationalNumber(3), new RationalNumber(-2), new RationalNumber(5), null),
                Arguments.of(new RationalNumber(-2), new RationalNumber(-3), ONE, null),
                Arguments.of(new RationalNumber(-3), new RationalNumber(-2), new RationalNumber(-1), null),
                Arguments.of(new RationalNumber(4, 3), new RationalNumber(1), new RationalNumber(1, 3), null)
        );
    }

    private static Stream<Arguments> multiplyUsingLong() {
        return Stream.of(
                Arguments.of(ZERO, 0L, ZERO),
                Arguments.of(ONE, 0L, ZERO),
                Arguments.of(ZERO, 1L, ZERO),
                Arguments.of(new RationalNumber(2), 3, new RationalNumber(6)),
                Arguments.of(new RationalNumber(3), 2, new RationalNumber(6)),
                Arguments.of(new RationalNumber(-2), 3, new RationalNumber(-6)),
                Arguments.of(new RationalNumber(3), -2, new RationalNumber(-6)),
                Arguments.of(new RationalNumber(-2), -3, new RationalNumber(6)),
                Arguments.of(new RationalNumber(-3), -2, new RationalNumber(6)),
                Arguments.of(new RationalNumber(4, 3), 1, new RationalNumber(4, 3), null)
        );
    }

    private static Stream<Arguments> multiplyUsingBigInteger() {
        return Stream.of(
                Arguments.of(ZERO, null, ZERO, "Please specify a multiplicand."),
                Arguments.of(ZERO, BigInteger.ZERO, ZERO, null),
                Arguments.of(ONE, BigInteger.ZERO, ZERO, null),
                Arguments.of(ZERO, BigInteger.ONE, ZERO, null),
                Arguments.of(new RationalNumber(2), BigInteger.valueOf(3), new RationalNumber(6), null),
                Arguments.of(new RationalNumber(3), BigInteger.valueOf(2), new RationalNumber(6), null),
                Arguments.of(new RationalNumber(-2), BigInteger.valueOf(3), new RationalNumber(-6), null),
                Arguments.of(new RationalNumber(3), BigInteger.valueOf(-2), new RationalNumber(-6), null),
                Arguments.of(new RationalNumber(-2), BigInteger.valueOf(-3), new RationalNumber(6), null),
                Arguments.of(new RationalNumber(-3), BigInteger.valueOf(-2), new RationalNumber(6), null),
                Arguments.of(new RationalNumber(4, 3), BigInteger.valueOf(1), new RationalNumber(4, 3), null)
        );
    }

    private static Stream<Arguments> multiplyUsingRationalNumber() {
        return Stream.of(
                Arguments.of(ZERO, null, ZERO, "Please specify a multiplicand."),
                Arguments.of(ZERO, ZERO, ZERO, null),
                Arguments.of(ONE, ZERO, ZERO, null),
                Arguments.of(ZERO, ONE, ZERO, null),
                Arguments.of(new RationalNumber(2), new RationalNumber(3), new RationalNumber(6), null),
                Arguments.of(new RationalNumber(3), new RationalNumber(2), new RationalNumber(6), null),
                Arguments.of(new RationalNumber(-2), new RationalNumber(3), new RationalNumber(-6), null),
                Arguments.of(new RationalNumber(3), new RationalNumber(-2), new RationalNumber(-6), null),
                Arguments.of(new RationalNumber(-2), new RationalNumber(-3), new RationalNumber(6), null),
                Arguments.of(new RationalNumber(-3), new RationalNumber(-2), new RationalNumber(6), null),
                Arguments.of(new RationalNumber(4, 3), new RationalNumber(1, 4), new RationalNumber(1, 3), null)
        );
    }

    private static Stream<Arguments> divideUsingLong() {
        return Stream.of(
                Arguments.of(ZERO, 0L, null, "Division by zero"),
                Arguments.of(ONE, 0L, null, "Division by zero"),
                Arguments.of(ZERO, 1L, ZERO, null),
                Arguments.of(new RationalNumber(2), 3, new RationalNumber(2, 3), null),
                Arguments.of(new RationalNumber(3), 2, new RationalNumber(3, 2), null),
                Arguments.of(new RationalNumber(-2), 3, new RationalNumber(-2, 3), null),
                Arguments.of(new RationalNumber(3), -2, new RationalNumber(3, -2), null),
                Arguments.of(new RationalNumber(-2), -3, new RationalNumber(-2, -3), null),
                Arguments.of(new RationalNumber(-3), -2, new RationalNumber(3, 2), null),
                Arguments.of(new RationalNumber(4, 3), 2, new RationalNumber(2, 3), null)
        );
    }

    private static Stream<Arguments> divideUsingBigInteger() {
        return Stream.of(
                Arguments.of(ZERO, BigInteger.ZERO, null, "Division by zero"),
                Arguments.of(ONE, BigInteger.ZERO, null, "Division by zero"),
                Arguments.of(ZERO, BigInteger.ONE, ZERO, null),
                Arguments.of(new RationalNumber(2), BigInteger.valueOf(3), new RationalNumber(2, 3), null),
                Arguments.of(new RationalNumber(3), BigInteger.valueOf(2), new RationalNumber(3, 2), null),
                Arguments.of(new RationalNumber(-2), BigInteger.valueOf(3), new RationalNumber(-2, 3), null),
                Arguments.of(new RationalNumber(3), BigInteger.valueOf(-2), new RationalNumber(3, -2), null),
                Arguments.of(new RationalNumber(-2), BigInteger.valueOf(-3), new RationalNumber(-2, -3), null),
                Arguments.of(new RationalNumber(-3), BigInteger.valueOf(-2), new RationalNumber(3, 2), null),
                Arguments.of(new RationalNumber(4, 3), BigInteger.valueOf(2), new RationalNumber(2, 3), null)
        );
    }

    private static Stream<Arguments> divideUsingRationalNumber() {
        return Stream.of(
                Arguments.of(ZERO, ZERO, null, "Division by zero"),
                Arguments.of(ONE, ZERO, null, "Division by zero"),
                Arguments.of(ZERO, ONE, ZERO, null),
                Arguments.of(new RationalNumber(2), new RationalNumber(3), new RationalNumber(2, 3), null),
                Arguments.of(new RationalNumber(3), new RationalNumber(2), new RationalNumber(3, 2), null),
                Arguments.of(new RationalNumber(-2), new RationalNumber(3), new RationalNumber(-2, 3), null),
                Arguments.of(new RationalNumber(3), new RationalNumber(-2), new RationalNumber(3, -2), null),
                Arguments.of(new RationalNumber(-2), new RationalNumber(-3), new RationalNumber(-2, -3), null),
                Arguments.of(new RationalNumber(-3), new RationalNumber(-2), new RationalNumber(3, 2), null),
                Arguments.of(new RationalNumber(4, 3), new RationalNumber(2), new RationalNumber(2, 3), null)
        );
    }

    private static Stream<Arguments> negate() {
        return Stream.of(
                Arguments.of(ZERO, ZERO),
                Arguments.of(ONE, new RationalNumber(-1)),
                Arguments.of(new RationalNumber(2), new RationalNumber(-2)),
                Arguments.of(new RationalNumber(-0), ZERO),
                Arguments.of(new RationalNumber(-1), ONE),
                Arguments.of(new RationalNumber(-2), new RationalNumber(2)),
                Arguments.of(new RationalNumber(3), new RationalNumber(-3)),
                Arguments.of(new RationalNumber(-3), new RationalNumber(3)),
                Arguments.of(new RationalNumber(-3, 4), new RationalNumber(3, 4)),
                Arguments.of(new RationalNumber(3, 4), new RationalNumber(-3, 4))
        );
    }

    private static Stream<Arguments> abs() {
        return Stream.of(
                Arguments.of(ZERO, ZERO),
                Arguments.of(ONE, ONE),
                Arguments.of(new RationalNumber(2), new RationalNumber(2)),
                Arguments.of(new RationalNumber(-0), ZERO),
                Arguments.of(new RationalNumber(-1), ONE),
                Arguments.of(new RationalNumber(-2), new RationalNumber(2)),
                Arguments.of(new RationalNumber(3), new RationalNumber(3)),
                Arguments.of(new RationalNumber(-3), new RationalNumber(3)),
                Arguments.of(new RationalNumber(-3, 4), new RationalNumber(3, 4)),
                Arguments.of(new RationalNumber(3, 4), new RationalNumber(3, 4))
        );
    }

    private static Stream<Arguments> numberString() {
        return Stream.of(
                Arguments.of("1", new RationalNumber(1)),
                Arguments.of("-1", new RationalNumber(-1)),
                Arguments.of("12.345", new RationalNumber(12345, 1000)),
                Arguments.of("-12.345", new RationalNumber(-12345, 1000)),
                Arguments.of("345E[12]", new RationalNumber(345000000000000L)),
                Arguments.of("0.[142857]R", new RationalNumber(1, 7)),
                Arguments.of("0.12[345]R", new RationalNumber(12345345 - 12345, 1000000000 - 1000000))
        );
    }

    private static Stream<Arguments> divideAndRemainder() {
        return Stream.of(
                Arguments.of(new RationalNumber(52, 10), new RationalNumber(25, 10), new RationalNumber(2), new RationalNumber(2, 10)),
                Arguments.of(new RationalNumber(8), new RationalNumber(1), new RationalNumber(8), ZERO),
                Arguments.of(new RationalNumber(8), new RationalNumber(2), new RationalNumber(4), ZERO),
                Arguments.of(new RationalNumber(8), new RationalNumber(3), new RationalNumber(2), new RationalNumber(2)),
                Arguments.of(new RationalNumber(8), new RationalNumber(4), new RationalNumber(2), ZERO),
                Arguments.of(new RationalNumber(8), new RationalNumber(5), ONE, new RationalNumber(3)),
                Arguments.of(new RationalNumber(8), new RationalNumber(6), ONE, new RationalNumber(2)),
                Arguments.of(new RationalNumber(8), new RationalNumber(7), ONE, ONE),
                Arguments.of(new RationalNumber(8), new RationalNumber(8), ONE, ZERO)
        );
    }

}

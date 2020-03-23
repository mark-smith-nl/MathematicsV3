package nl.smith.mathematics.numbertype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import static nl.smith.mathematics.numbertype.RationalNumber.ZERO;
import static nl.smith.mathematics.numbertype.RationalNumber.ONE;

public class RationalNumberTest {

    @Test
    void constructorUsingInteger() {
        assertEquals(new RationalNumber(2).getNumerator(), BigInteger.valueOf(2));
    }

    @Test
    void constructorUsingIntegers() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        assertEquals(rationalNumber.getNumerator(), BigInteger.valueOf(2));
        assertEquals(rationalNumber.getDenominator(), BigInteger.valueOf(3));
    }

    @Test
    void constructorUsingZeroDenominatorInteger() {
        Exception exception = assertThrows(ArithmeticException.class, () ->  new RationalNumber(2, 0));
        assertEquals(exception.getMessage(), "Division by zero");
    }

    @Test
    void constructorUsingBigInteger() {
        assertEquals(new RationalNumber(BigInteger.valueOf(2)).getNumerator(), BigInteger.valueOf(2));
    }

    @Test
    void constructorUsingNullBigInteger() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new RationalNumber((BigInteger) null));
        assertEquals(exception.getMessage(), "Both numerator and denominator must be specified (not be null)");
    }

    @Test
    void constructorUsingBigIntegers() {
        RationalNumber rationalNumber = new RationalNumber(BigInteger.valueOf(2), BigInteger.valueOf(6));
        assertEquals(rationalNumber.getNumerator(), BigInteger.valueOf(2));
        assertEquals(rationalNumber.getDenominator(), BigInteger.valueOf(6));
    }

    @Test
    void constructorUsingBigIntegersDenominatorZero() {
        Exception exception = assertThrows(ArithmeticException.class, () -> new RationalNumber(BigInteger.valueOf(2), BigInteger.valueOf(0)));
        assertEquals(exception.getMessage(), "Division by zero");
    }

    @Test
    void constructorUsingIllegalDenominator() {
        Exception exception = assertThrows(ArithmeticException.class, () -> new RationalNumber(BigInteger.valueOf(2), BigInteger.valueOf(0)));
        assertEquals(exception.getMessage(), "Division by zero") ;
    }

    @DisplayName("Testing the construction of a RationalNumber using its static public method valueOf()")
    @ParameterizedTest
    @MethodSource("numberString")
    void valueOf(String numberString) {
        RationalNumber.valueOf(numberString);
    }

    @Test
    void add() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber augend = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.add(augend);

        assertEquals(result, ONE);
    }

    @Test
    void subtract() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber subtrahend = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.subtract(subtrahend);

        assertEquals(result, new RationalNumber(1, 3));
    }

    @Test
    void multiply() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber multiplicand = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.multiply(multiplicand);

        assertEquals(result, new RationalNumber(6, 27));
    }

    @Test
    void multiply_usingBigInteger() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        BigInteger multiplicand = new BigInteger("2");
        RationalNumber result = rationalNumber.multiply(multiplicand);

        assertEquals(result, new RationalNumber(4, 3));
    }

    @Test
    void divide() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber divisor = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.divide(divisor);

        assertEquals(result, new RationalNumber(2));
    }

    @Test
    void divideByZero() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber divisor = new RationalNumber(0);

        assertThrows(ArithmeticException.class, () -> rationalNumber.divide(divisor));
    }

    @Test
    void equalsCompareWithNull() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);

        assertFalse(rationalNumber.equals(null));
    }

    @Test
    void equalsCompareWithString() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);

        assertFalse(rationalNumber.equals("String"));
    }

    @Test
    void equalsCompare() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber otherRationalNumber = new RationalNumber(20, 30);

        assertTrue(rationalNumber.equals(otherRationalNumber));
    }

    @ParameterizedTest
    @MethodSource("divideAndRemainder")
    void divideAndRemainder(RationalNumber number, RationalNumber divisor, RationalNumber integerValue, RationalNumber remainder) {
        RationalNumber[] divideAndRemainder = number.divideAndRemainder(divisor);
        assertEquals(integerValue, divideAndRemainder[0]);
        assertEquals(remainder, divideAndRemainder[1]);
    }

    private static Stream<Arguments> numberString() {
        return Stream.of(
                Arguments.of("1", new RationalNumber(1)),
                Arguments.of("-1", new RationalNumber(-1)),
                Arguments.of("12.345", new RationalNumber(12345, 1000)),
                Arguments.of("-12.345", new RationalNumber(-12345, 1000)),
                Arguments.of("345E[12]", new RationalNumber(345000000000000l)),
                Arguments.of("0.{142857}R", new RationalNumber(1, 7)),
                Arguments.of("0.12{345}R", new RationalNumber(12345345 - 12345, 1000000000 - 1000000)));
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
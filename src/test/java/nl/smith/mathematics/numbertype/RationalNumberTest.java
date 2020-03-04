package nl.smith.mathematics.numbertype;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void add() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber augend = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.add(augend);

        assertEquals(result.getNumerator(), BigInteger.valueOf(27));
        assertEquals(result.getDenominator(), BigInteger.valueOf(27));
    }

    @Test
    void subtract() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber subtrahend = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.subtract(subtrahend);

        assertEquals(result.getNumerator(), BigInteger.valueOf(9));
        assertEquals(result.getDenominator(), BigInteger.valueOf(27));
    }

    @Test
    void multiply() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber multiplicand = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.multiply(multiplicand);

        assertEquals(result.getNumerator(), BigInteger.valueOf(6));
        assertEquals(result.getDenominator(), BigInteger.valueOf(27));
    }

    @Test
    void divide() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber divisor = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.divide(divisor);

        assertEquals(result.getNumerator(), BigInteger.valueOf(18));
        assertEquals(result.getDenominator(), BigInteger.valueOf(9));
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
}
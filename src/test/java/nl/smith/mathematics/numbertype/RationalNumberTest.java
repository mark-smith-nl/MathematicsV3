package nl.smith.mathematics.numbertype;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RationalNumberTest {

    @Test
    public void constructorUsingInteger() {
        assertEquals(new RationalNumber(2).getNumerator(), BigInteger.valueOf(2));
    }

    @Test
    public void constructorUsingIntegers() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        assertEquals(rationalNumber.getNumerator(), BigInteger.valueOf(2));
        assertEquals(rationalNumber.getDenominator(), BigInteger.valueOf(3));
    }

    @Test
    public void constructorUsingBigInteger() {
        assertEquals(new RationalNumber(BigInteger.valueOf(2)).getNumerator(), BigInteger.valueOf(2));
    }

    @Test
    public void constructorUsingBigIntegers() {
        RationalNumber rationalNumber = new RationalNumber(BigInteger.valueOf(2), BigInteger.valueOf(6));
        assertEquals(rationalNumber.getNumerator(), BigInteger.valueOf(2));
        assertEquals(rationalNumber.getDenominator(), BigInteger.valueOf(6));
    }

    @Test
    public void constructorUsingBigIntegersDenominatorZero() {
        assertThrows(ArithmeticException.class, () -> new RationalNumber(BigInteger.valueOf(2), BigInteger.valueOf(0)));
    }

    @Test
    public void constructorUsingRationalNumber() {
        assertThrows(ArithmeticException.class, () -> {
            new RationalNumber(BigInteger.valueOf(2), BigInteger.valueOf(0));
        });
    }

    @Test
    public void add() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber augend = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.add(augend);

        assertEquals(result.getNumerator(), BigInteger.valueOf(27));
        assertEquals(result.getDenominator(), BigInteger.valueOf(27));
    }

    @Test
    public void subtract() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber subtrahend = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.subtract(subtrahend);

        assertEquals(result.getNumerator(), BigInteger.valueOf(9));
        assertEquals(result.getDenominator(), BigInteger.valueOf(27));
    }

    @Test
    public void multiply() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber multiplicand = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.multiply(multiplicand);

        assertEquals(result.getNumerator(), BigInteger.valueOf(6));
        assertEquals(result.getDenominator(), BigInteger.valueOf(27));
    }

    @Test
    public void divide() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber divisor = new RationalNumber(3, 9);
        RationalNumber result = rationalNumber.divide(divisor);

        assertEquals(result.getNumerator(), BigInteger.valueOf(18));
        assertEquals(result.getDenominator(), BigInteger.valueOf(9));
    }

    @Test
    public void divideByZero() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber divisor = new RationalNumber(0);

        assertThrows(ArithmeticException.class, () -> rationalNumber.divide(divisor));
    }

    @Test
    public void equalsCompareWithNull() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);

        assertFalse(rationalNumber.equals(null));
    }

    @Test
    public void equalsCompareWithString() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);

        assertFalse(rationalNumber.equals("String"));
    }

    @Test
    public void equalsCompare() {
        RationalNumber rationalNumber = new RationalNumber(2, 3);
        RationalNumber otherRationalNumber = new RationalNumber(20, 30);

        assertTrue(rationalNumber.equals(otherRationalNumber));
    }
}
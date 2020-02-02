package nl.smith.mathematics.numbertype;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RationalNumberTest {

    @Test
    public void constructorUsingInteger() {
        assertEquals(new RationalNumber(2).getNumerator(), BigInteger.valueOf(2));
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
        assertThrows(ArithmeticException.class, () -> {
            new RationalNumber(BigInteger.valueOf(2), BigInteger.valueOf(0));
        });
    }

    @Test
    public void constructorUsingRationalNumber() {
        assertThrows(ArithmeticException.class, () -> {
            new RationalNumber(BigInteger.valueOf(2), BigInteger.valueOf(0));
        });
    }

}
package nl.smith.mathematics.numbertype;

import nl.smith.mathematics.util.RationalNumberUtil;

import java.math.BigInteger;

/** Immutable class to store rational numbers
 * numerator ∊ ℤ
 * denominator ℤ+
 */
public class RationalNumber extends Number implements ArithmeticFunctions<RationalNumber> {

    public static final RationalNumber ZERO = new RationalNumber(BigInteger.ZERO, BigInteger.ONE);

    public static final RationalNumber ONE = new RationalNumber(BigInteger.ONE, BigInteger.ONE);

    public static final RationalNumber TEN = new RationalNumber(BigInteger.TEN, BigInteger.ONE);

    private final BigInteger numerator;

    private final BigInteger denominator;

    public RationalNumber(int numerator) {
        this(BigInteger.valueOf(numerator));
    }

    public RationalNumber(BigInteger numerator) {
        this(numerator, BigInteger.ONE);
    }

    public RationalNumber(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public RationalNumber(RationalNumber rationalNumber) {
        this(rationalNumber.numerator, rationalNumber.denominator);
    }

    public RationalNumber(BigInteger numerator, BigInteger denominator) {
        if (numerator == null || denominator == null) {
            throw new IllegalArgumentException("Both numerator and denominator must be specified (not be null)");
        }

        if (denominator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Division by zero");
        }

        if (denominator.compareTo(BigInteger.ZERO) < 0) {
            numerator = numerator.negate();
            denominator = denominator.abs();
        }

        this.numerator = numerator;
        this.denominator = denominator;
    }

    public RationalNumber[] divideAndRemainder() {
        BigInteger[] divideAndRemainder = numerator.divideAndRemainder(denominator);
        return new RationalNumber[]{new RationalNumber(divideAndRemainder[0]), new RationalNumber(divideAndRemainder[1], denominator)};
    }

    @Override
    public int intValue() {
        return divideAndRemainder()[0].numerator.intValue();
    }

    @Override
    public long longValue() {
        return divideAndRemainder()[0].numerator.longValue();
    }

    @Override
    public float floatValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double doubleValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    public RationalNumber add(RationalNumber augend) {
        BigInteger numerator = this.numerator.multiply(augend.denominator).add(augend.numerator.multiply(this.denominator));
        BigInteger denominator = this.denominator.multiply(augend.denominator);

        return new RationalNumber(numerator, denominator);
    }

    @Override
    public RationalNumber subtract(RationalNumber subtrahend) {
        BigInteger numerator = this.numerator.multiply(subtrahend.denominator).subtract(subtrahend.numerator.multiply(this.denominator));
        BigInteger denominator = this.denominator.multiply(subtrahend.denominator);

        return new RationalNumber(numerator, denominator);
    }

    public RationalNumber multiply(RationalNumber multiplicand) {
        BigInteger numerator = this.numerator.multiply(multiplicand.numerator);
        BigInteger denominator = this.denominator.multiply(multiplicand.denominator);

        return new RationalNumber(numerator, denominator);
    }

    public RationalNumber divide(RationalNumber divisor) {
        BigInteger numerator = this.numerator.multiply(divisor.denominator);
        BigInteger denominator = this.denominator.multiply(divisor.numerator);

        return new RationalNumber(numerator, denominator);
    }

    @Override
    public RationalNumber negate() {
        return new RationalNumber(numerator.negate(), denominator);
    }

    @Override
    public RationalNumber abs() {
        return new RationalNumber(numerator.abs(), denominator);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        RationalNumber rationalNumber = (RationalNumber) obj;

        return numerator.multiply(rationalNumber.denominator).equals(rationalNumber.numerator.multiply(denominator));
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public static RationalNumber valueOf(String numberString) {
        return RationalNumberUtil.getRationalNumber(numberString);
    }
}

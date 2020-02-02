package nl.smith.mathematics.numbertype;

import java.math.BigInteger;

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

	public RationalNumber(BigInteger numerator, BigInteger denominator) {
		if (denominator.equals(BigInteger.ZERO)) {
			throw new ArithmeticException("Division by zero");
		}
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public RationalNumber(long numerator, long denominator) {
		this.numerator = BigInteger.valueOf(numerator);
		this.denominator = BigInteger.valueOf(denominator);
	}

	public RationalNumber(RationalNumber rationalNumber) {
		this(rationalNumber.numerator, rationalNumber.denominator);
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

	public BigInteger getNumerator() {
		return numerator;
	}

	public BigInteger getDenominator() {
		return denominator;
	}

	public static RationalNumber valueOf(String value) {
		return null;
	}
}

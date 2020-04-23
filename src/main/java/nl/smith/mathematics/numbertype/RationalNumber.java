package nl.smith.mathematics.numbertype;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.configuration.constant.Scale;
import nl.smith.mathematics.util.RationalNumberUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable class to store rational numbers
 * <p>
 * numerator ∊ ℤ
 * denominator ℤ+
 * <p>
 * Note: Numbers are not normalized by default i.e. 2/10 will not be converted to 1/5.
 */
public class RationalNumber extends ArithmeticFunctions<RationalNumber> implements Comparable<RationalNumber> {

    public static final RationalNumber ZERO = new RationalNumber(BigInteger.ZERO, BigInteger.ONE);

    public static final RationalNumber ONE = new RationalNumber(BigInteger.ONE, BigInteger.ONE);

    public static final RationalNumber TEN = new RationalNumber(BigInteger.TEN, BigInteger.ONE);

    private final BigInteger numerator;

    private final BigInteger denominator;

    public RationalNumber(long numerator) {
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

    public RationalNumber normalized() {
        BigInteger gcd = numerator.gcd(denominator);

        return new RationalNumber(numerator.divide(gcd), denominator.divide(gcd));
    }

    public RationalNumber[] divideAndRemainder(RationalNumber divisor) {
        RationalNumber division = this.divide(divisor);
        BigInteger integerPart = division.bigIntValue();
        RationalNumber remainder = this.subtract(divisor.multiply(integerPart));

        return new RationalNumber[]{new RationalNumber(integerPart), remainder};
    }

    @Override
    public boolean isNaturalNumber() {
        return numerator.divideAndRemainder(denominator)[1].equals(BigInteger.ZERO);
    }

    @Override
    public int signum() {
        return numerator.signum();
    }

    @Override
    public int intValue() {
        return numerator.divideAndRemainder(denominator)[0].intValue();
    }

    public BigInteger bigIntValue() {
        return numerator.divideAndRemainder(denominator)[0];
    }

    @Override
    public long longValue() {
        return numerator.divideAndRemainder(denominator)[0].longValue();
    }

    //TODO Specify scale
    @Override
    public float floatValue() {
        return (new BigDecimal(numerator)).divide(new BigDecimal(denominator)).floatValue();
    }

    //TODO Implement doubleValue()
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

    public RationalNumber multiply(BigInteger multiplicand) {
        BigInteger numerator = this.numerator.multiply(multiplicand);

        return new RationalNumber(numerator, denominator);
    }

    public RationalNumber divide(RationalNumber divisor) {
        BigInteger numerator = this.numerator.multiply(divisor.denominator);
        BigInteger denominator = this.denominator.multiply(divisor.numerator);

        return new RationalNumber(numerator, denominator);
    }

    public RationalNumber divide(long divisor) {
        return divide(new RationalNumber(divisor));
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

    @Override
    public String toString() {
        return toString(RationalNumberOutputType.get());
    }

    public String toStringComponents() {
        return numerator.toString() + "/" + denominator.toString();
    }

    public String toStringExact() {
        if (signum() >= 0) {
            BigInteger[] bigIntegers = numerator.divideAndRemainder(denominator);

            StringBuilder result = new StringBuilder(bigIntegers[0].toString());

            BigInteger remainder = bigIntegers[1];
            StringBuilder fractionalPart = new StringBuilder();
            Map<BigInteger, Integer> resultDivisionAtPosition = new HashMap<>();
            int position = 0;

            while (!remainder.equals(BigInteger.ZERO)) {
                remainder = remainder.multiply(BigInteger.TEN);
                Integer positionStartRepetition = resultDivisionAtPosition.get(remainder);
                if (positionStartRepetition != null) {
                    fractionalPart.insert(positionStartRepetition, "{");
                    fractionalPart.append("}R");
                    remainder = BigInteger.ZERO;
                } else {
                    resultDivisionAtPosition.put(remainder, position);
                    bigIntegers = remainder.divideAndRemainder(denominator);
                    fractionalPart.append(bigIntegers[0].toString());
                    remainder = bigIntegers[1];
                    ++position;
                }
            }

            if (fractionalPart.length() > 0) {
                result.append(".");
                result.append(fractionalPart);
            }

            return result.toString();
        } else {
            return "-" + abs().toStringExact();
        }
    }

    public String toStringTruncated(int scale) {
        if (signum() >= 0) {
            BigInteger[] bigIntegers = numerator.divideAndRemainder(denominator);

            StringBuilder result = new StringBuilder(bigIntegers[0].toString());

            BigInteger remainder = bigIntegers[1];
            StringBuilder fractionalPart = new StringBuilder();
            Map<BigInteger, Integer> resultDivisionAtPosition = new HashMap<>();
            int position = 0;

            while (!remainder.equals(BigInteger.ZERO) && position < scale) {
                remainder = remainder.multiply(BigInteger.TEN);
                Integer positionStartRepetition = resultDivisionAtPosition.get(remainder);
                if (positionStartRepetition != null) {
                    int totalDecimalsToAppend = scale - fractionalPart.length();
                    String repeatingFractionalPart = fractionalPart.substring(positionStartRepetition);
                    for (int i = 0; i < totalDecimalsToAppend / repeatingFractionalPart.length(); i++) {
                        fractionalPart.append(repeatingFractionalPart);
                    }

                    fractionalPart.append(repeatingFractionalPart.substring(0, totalDecimalsToAppend % repeatingFractionalPart.length()));
                    remainder = BigInteger.ZERO;
                } else {
                    resultDivisionAtPosition.put(remainder, position);
                    bigIntegers = remainder.divideAndRemainder(denominator);
                    fractionalPart.append(bigIntegers[0].toString());
                    remainder = bigIntegers[1];
                    ++position;
                }
            }

            if (fractionalPart.length() > 0) {
                result.append(".");
                result.append(fractionalPart);
            }

            return result.toString();
        } else {
            return "-" + abs().toStringTruncated(scale);
        }
    }

    public String toString(RationalNumberOutputType.Type outputType) {
        int scale = Scale.get();

        String result;
        switch (outputType) {
            case COMPONENTS:
                result = toStringComponents();
                break;
            case EXACT:
                result = toStringExact();
                break;
            case COMPONENTS_AND_EXACT:
                result = toStringComponents() + " ---> " + toStringExact();
                break;
            case TRUNCATED:
                result = toStringTruncated(scale);
                break;
            case ALL:
                String stringExact = toStringExact();
                result = toStringComponents() + " ---> " + toStringExact();
                String stringTruncated = toStringTruncated(scale);
                if (!stringExact.equals(stringTruncated)) {
                    result = result + " ~ " + stringTruncated + " (scale = "+ scale + ")";
                }
                break;
            default:
                result = String.format("toString(%s) is not implemented", outputType.name());
        }

        return result;
    }

    @Override
    public int compareTo(RationalNumber o) {
        return numerator.multiply(o.denominator).compareTo(o.numerator.multiply(denominator));
    }

    public static void main(String[] args) {
        System.out.println(new RationalNumber(20, 4));
    }
}

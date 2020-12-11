package nl.smith.mathematics.numbertype;

import nl.smith.mathematics.configuration.constant.RationalNumberNormalize;
import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.configuration.constant.RoundingMode;
import nl.smith.mathematics.util.RationalNumberUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static nl.smith.mathematics.configuration.constant.NumberConstant.integerValueOf.Scale;

/**
 * Immutable class to store rational numbers
 * <p>
 * numerator ∊ ℤ
 * denominator ℤ+
 * <p>
 * Note: Unless specified (see: {@link RationalNumberNormalize)} numbers are not normalized by default i.e. 2/10 will not be converted to 1/5.
 */
public class RationalNumber extends ArithmeticOperations<RationalNumber> implements Comparable<RationalNumber> {

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
        if (rationalNumber == null) {
            throw new IllegalArgumentException("A rational number must be specified (not be null)");
        }

        BigInteger n = rationalNumber.numerator;
        BigInteger d = rationalNumber.denominator;

        if (Boolean.TRUE.equals(RationalNumberNormalize.get())) {
            BigInteger[] normalizedComponents = getNormalizedComponents(n, d);
            n = normalizedComponents[0];
            d = normalizedComponents[1];
        }

        this.numerator = n;
        this.denominator = d;
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

        if (Boolean.TRUE.equals(RationalNumberNormalize.get())) {
            BigInteger[] normalizedComponents = getNormalizedComponents(numerator, denominator);
            numerator = normalizedComponents[0];
            denominator = normalizedComponents[1];
        }

        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Protected for test purposes.
     */
    protected static BigInteger[] getNormalizedComponents(BigInteger numerator, BigInteger denominator) {
        // Note: The gcd is a positive number
        BigInteger gcd = numerator.gcd(denominator);

        if (gcd.equals(BigInteger.ONE)) {
            return new BigInteger[]{numerator, denominator};
        }

        return new BigInteger[]{numerator.divide(gcd), denominator.divide(gcd)};
    }


    /** Returns the current instance if it is normalized else returns a new normalized rational number instance.*/
    public RationalNumber getNormalized() {
        BigInteger[] normalizedComponents = getNormalizedComponents(numerator, denominator);

        return normalizedComponents[1].equals(denominator) ? this : new RationalNumber(normalizedComponents[0], normalizedComponents[1]);
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

    /** The truncated value as {@link java.math.BigInteger} */
    public BigInteger bigIntValue() {
        return numerator.divideAndRemainder(denominator)[0];
    }

    @Override
    public long longValue() {
        return numerator.divideAndRemainder(denominator)[0].longValue();
    }

    //TODO Test
    @Override
    public float floatValue() {
        return (new BigDecimal(numerator)).divide(new BigDecimal(denominator), Scale.get(), RoundingMode.get()).floatValue();
    }

    //TODO Test
    @Override
    public double doubleValue() {
        return (new BigDecimal(numerator)).divide(new BigDecimal(denominator), Scale.get(), RoundingMode.get()).doubleValue();
    }

    @Override
    public RationalNumber add(long augend) {
        return add(BigInteger.valueOf(augend));
    }

    @Override
    public RationalNumber add(BigInteger augend) {
        if (augend == null) {
            throw new ArithmeticException("Please specify an augend.");
        }

        return add(new RationalNumber(augend));
    }

    @Override
    public RationalNumber add(RationalNumber augend) {
        if (augend == null) {
            throw new ArithmeticException("Please specify an augend.");
        }

        BigInteger n = this.numerator.multiply(augend.denominator).add(augend.numerator.multiply(this.denominator));
        BigInteger d = this.denominator.multiply(augend.denominator);

        return new RationalNumber(n, d);
    }

    @Override
    public RationalNumber subtract(long subtrahend) {
        return subtract(BigInteger.valueOf(subtrahend));
    }

    @Override
    public RationalNumber subtract(BigInteger subtrahend) {
        if (subtrahend == null) {
            throw new ArithmeticException("Please specify a subtrahend.");
        }

        return subtract(new RationalNumber(subtrahend));
    }
    @Override
    public RationalNumber subtract(RationalNumber subtrahend) {
        if (subtrahend == null) {
            throw new ArithmeticException("Please specify a subtrahend.");
        }

        BigInteger n = this.numerator.multiply(subtrahend.denominator).subtract(subtrahend.numerator.multiply(this.denominator));
        BigInteger d = this.denominator.multiply(subtrahend.denominator);

        return new RationalNumber(n, d);
    }

    @Override
    public RationalNumber multiply(long multiplicand) {
        return multiply(BigInteger.valueOf(multiplicand));
    }

    @Override
    public RationalNumber multiply(BigInteger multiplicand) {
        if (multiplicand == null) {
            throw new ArithmeticException("Please specify a multiplicand.");
        }

        return multiply(new RationalNumber(multiplicand));
    }

    @Override
    public RationalNumber multiply(RationalNumber multiplicand) {
        if (multiplicand == null) {
            throw new ArithmeticException("Please specify a multiplicand.");
        }

        BigInteger n = this.numerator.multiply(multiplicand.numerator);
        BigInteger d = this.denominator.multiply(multiplicand.denominator);

        return new RationalNumber(n, d);
    }

    @Override
    public RationalNumber divide(long divisor) {
        return divide(BigInteger.valueOf(divisor));
    }

    @Override
    public RationalNumber divide(BigInteger divisor) {
        if (divisor == null) {
            throw new ArithmeticException("Please specify a divisor.");
        }

        return divide(new RationalNumber(divisor));
    }

    @Override
    public RationalNumber divide(RationalNumber divisor) {
        if (divisor == null) {
            throw new ArithmeticException("Please specify a divisor.");
        }

        BigInteger n = this.numerator.multiply(divisor.denominator);
        BigInteger d = this.denominator.multiply(divisor.numerator);

        return new RationalNumber(n, d);
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
                    fractionalPart.insert(positionStartRepetition, "[");
                    fractionalPart.append("]R");
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

                    fractionalPart.append(repeatingFractionalPart, 0, totalDecimalsToAppend % repeatingFractionalPart.length());
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

    public String toString(RationalNumberOutputType.PredefinedType outputPredefinedType) {
        int scale = Scale.get();

        String result;
        switch (outputPredefinedType) {
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
                result = format("toString(%s) is not implemented", outputPredefinedType.name());
        }

        return result;
    }

    @Override
    public int compareTo(RationalNumber o) {
        return numerator.multiply(o.denominator).compareTo(o.numerator.multiply(denominator));
    }

}

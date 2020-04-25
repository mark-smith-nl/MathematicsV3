package nl.smith.mathematics.util;

import nl.smith.mathematics.numbertype.RationalNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Utility to inspect a string to determine if it could be transformed into a {@link nl.smith.mathematics.numbertype.RationalNumber}
 *
 * <pre>
 *         ------     ------
 *     ->-| SIGN |->-| 1...9 |----
 *         ------   | ------   |
 *                  ^          |
 *                  |          |
 *                   ----------
 * </pre>
 * */

public class RationalNumberUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(RationalNumberUtil.class);

    public enum NumberComponent {
        SIGN_PART,
        POSITIVE_INTEGER_PART,
        CONSTANT_FRACTIONAL_PART,
        REPEATING_FRACTIONAL_PART,
        SIGN_EXPONENTIAL_PART,
        POSITIVE_EXPONENTIAL_PART;
    }

    public static final Pattern NUMBER_PATTERN = Pattern.compile("((\\-)?([1-9]\\d*)(\\.(\\d*)(([1-9])|(\\{(\\d*[1-9]\\d*)\\}R)))?(E\\[(\\-)?(\\d{2})\\])?)" +
            "|(0)|((\\-)?0(\\.(\\d*)(([1-9])|(\\{(\\d*[1-9]\\d*)\\}R)))(E\\[(\\-)?(\\d{2})\\])?)");

    private RationalNumberUtil() {
        throw new IllegalStateException(String.format("Can not instantiate %s", this.getClass().getCanonicalName()));
    }

    public static void assertIsNumber(String numberString) {
        if (numberString == null || numberString.isEmpty() || !NUMBER_PATTERN.matcher(numberString).matches()) {
            throw new IllegalArgumentException("Number string is null, empty or does not represent a number");
        }
    }

    public static RationalNumber getRationalNumber(String numberString) {
        Map<NumberComponent, String> numberComponents = getNumberComponents(numberString);

        BigInteger numerator;
        BigInteger denominator;

        if (numberComponents.get(NumberComponent.REPEATING_FRACTIONAL_PART) == null) {
            numerator = new BigInteger(numberComponents.getOrDefault(NumberComponent.POSITIVE_INTEGER_PART, "")
                    .concat(numberComponents.getOrDefault(NumberComponent.CONSTANT_FRACTIONAL_PART, "")));
            denominator = BigInteger.TEN.pow(numberComponents.getOrDefault(NumberComponent.CONSTANT_FRACTIONAL_PART, "").length());
        } else {
            numerator = (new BigInteger(numberComponents.getOrDefault(NumberComponent.POSITIVE_INTEGER_PART, "")
                    .concat(numberComponents.getOrDefault(NumberComponent.CONSTANT_FRACTIONAL_PART, ""))
                    .concat(numberComponents.getOrDefault(NumberComponent.REPEATING_FRACTIONAL_PART, ""))))
                    .subtract(new BigInteger(numberComponents.getOrDefault(NumberComponent.POSITIVE_INTEGER_PART, "")
                            .concat(numberComponents.getOrDefault(NumberComponent.CONSTANT_FRACTIONAL_PART, ""))));

            denominator = BigInteger.TEN.pow(numberComponents.getOrDefault(NumberComponent.CONSTANT_FRACTIONAL_PART, "").length()
                    + numberComponents.getOrDefault(NumberComponent.REPEATING_FRACTIONAL_PART, "").length())
                    .subtract(BigInteger.TEN.pow(numberComponents.getOrDefault(NumberComponent.CONSTANT_FRACTIONAL_PART, "").length()));
        }

        if (numberComponents.get(NumberComponent.POSITIVE_EXPONENTIAL_PART) != null) {
            BigInteger exponent = BigInteger.TEN.pow(Integer.valueOf(numberComponents.get(NumberComponent.POSITIVE_EXPONENTIAL_PART)));
            if (numberComponents.get(NumberComponent.SIGN_EXPONENTIAL_PART) == null) {
                numerator = numerator.multiply(exponent);
            } else {
                denominator = denominator.multiply(exponent);
            }
        }

        if (numberComponents.get(NumberComponent.SIGN_PART) != null) {
            numerator = numerator.negate();
        }

        return new RationalNumber(numerator, denominator);

    }

    public static Map<NumberComponent, String> getNumberComponents(String numberString) {
        assertIsNumber(numberString);

        Map<NumberComponent, String> numberComponents = new LinkedHashMap<>();
        Matcher matcher = NUMBER_PATTERN.matcher(numberString);

        if (matcher.matches()) {
            if (matcher.group(1) != null) {
                if (matcher.group(2) != null) {
                    numberComponents.put(NumberComponent.SIGN_PART, matcher.group(2));
                }
                numberComponents.put(NumberComponent.POSITIVE_INTEGER_PART, matcher.group(3));

                if (matcher.group(4) != null) {
                    String constantFractionalPart = matcher.group(5);
                    if (matcher.group(7) != null) {
                        constantFractionalPart += matcher.group(7);
                        numberComponents.put(NumberComponent.CONSTANT_FRACTIONAL_PART, constantFractionalPart);
                    } else {
                        numberComponents.put(NumberComponent.CONSTANT_FRACTIONAL_PART, constantFractionalPart);
                        numberComponents.put(NumberComponent.REPEATING_FRACTIONAL_PART, matcher.group(9));
                    }
                }
                if (matcher.group(10) != null) {
                    if (matcher.group(11) != null) {
                        numberComponents.put(NumberComponent.SIGN_EXPONENTIAL_PART, matcher.group(11));
                    }
                    numberComponents.put(NumberComponent.POSITIVE_EXPONENTIAL_PART, matcher.group(12));
                }
            } else if (matcher.group(13) != null) {
                numberComponents.put(NumberComponent.POSITIVE_INTEGER_PART, matcher.group(13));
            } else {
                if (matcher.group(15) != null){
                    numberComponents.put(NumberComponent.SIGN_PART, matcher.group(15));
                }
                numberComponents.put(NumberComponent.POSITIVE_INTEGER_PART, "0");
                String constantFractionalPart = matcher.group(17);
                if (matcher.group(19) != null) {
                    constantFractionalPart += matcher.group(19);
                    numberComponents.put(NumberComponent.CONSTANT_FRACTIONAL_PART, constantFractionalPart);
                } else {
                    numberComponents.put(NumberComponent.CONSTANT_FRACTIONAL_PART, constantFractionalPart);
                    numberComponents.put(NumberComponent.REPEATING_FRACTIONAL_PART, matcher.group(21));
                }
                if (matcher.group(22) != null) {
                    if (matcher.group(23) != null) {
                        numberComponents.put(NumberComponent.SIGN_EXPONENTIAL_PART, matcher.group(23));
                    }
                    numberComponents.put(NumberComponent.POSITIVE_EXPONENTIAL_PART, matcher.group(24));
                }
            }

            LOGGER.debug("Inspected number {}", numberString);
            numberComponents.entrySet().stream().forEach(e -> LOGGER.debug(String.format("%s ---> %s", e.getKey().name(), e.getValue())));
            return numberComponents;
        }

        throw new IllegalArgumentException("Not a number");
    }

}

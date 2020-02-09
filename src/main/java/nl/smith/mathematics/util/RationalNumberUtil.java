package nl.smith.mathematics.util;

import nl.smith.mathematics.numbertype.RationalNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    ;

    protected static final Pattern NUMBER_PATTERN = Pattern.compile("((\\-)?([1-9]\\d*)(\\.(\\d*)(([1-9])|(\\{(\\d*[1-9]\\d*)\\}R)))?(E\\[(\\-)?(\\d{2})\\])?)" +
            "|(0)|((\\-)?0(\\.(\\d*)(([1-9])|(\\{(\\d*[1-9]\\d*)\\}R)))(E\\[(\\-)?(\\d{2})\\])?)");

    private static void checkArgument(String numberString) {
        if (numberString == null || numberString.isEmpty()) {
            throw new IllegalArgumentException("Null or empty number string");
        }
    }

    public static boolean isNumber(String numberString) {
        checkArgument(numberString);

        return NUMBER_PATTERN.matcher(numberString).matches();
    }

    public static RationalNumber getRationalNumber(String numberString) {
        checkArgument(numberString);

        Map<NumberComponent, String> numberComponents = getNumberComponents(numberString);

        BigInteger numerator = (new BigInteger(numberComponents.get(NumberComponent.POSITIVE_INTEGER_PART)
                .concat(numberComponents.get(NumberComponent.CONSTANT_FRACTIONAL_PART))
                .concat(numberComponents.get(NumberComponent.REPEATING_FRACTIONAL_PART))))
                .subtract(new BigInteger(numberComponents.get(NumberComponent.POSITIVE_INTEGER_PART)
                        .concat(numberComponents.get(NumberComponent.CONSTANT_FRACTIONAL_PART))));

        BigInteger denominator = BigInteger.TEN.pow(numberComponents.get(NumberComponent.CONSTANT_FRACTIONAL_PART).length()
                + numberComponents.get(NumberComponent.REPEATING_FRACTIONAL_PART).length())

                .subtract(BigInteger.TEN.pow(numberComponents.get(NumberComponent.CONSTANT_FRACTIONAL_PART).length()));
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

    private static Map<NumberComponent, String> getNumberComponents(String numberString) {
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

            LOGGER.info("Inspected number {}", numberString);
            numberComponents.entrySet().stream().forEach(e -> LOGGER.info(String.format("%s ---> %s", e.getKey().name(), e.getValue())));
            return numberComponents;
        }

        throw new IllegalArgumentException("Not a number");
    }

}

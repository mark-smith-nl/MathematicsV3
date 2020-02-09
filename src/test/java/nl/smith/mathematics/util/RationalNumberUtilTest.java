package nl.smith.mathematics.util;

import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.AbstractMap;
import java.util.Stack;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
public class RationalNumberUtilTest {

    private static String[] NUMBERS = {"0",
            "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9",
            "10", "20", "30", "40", "50", "60", "70", "80", "90",
            "-10", "-20", "-30", "-40", "-50", "-60", "-70", "-80", "-90",
            "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9",
            "0.01", "0.02", "0.03", "0.04", "0.05", "0.06", "0.07", "0.08", "0.09",
            "10.01", "10.02", "10.03", "10.04", "10.05", "10.06", "10.07", "10.08", "10.09",
            "0.{1}R", "0.{01}R", "0.{10}R", "0.{001}R"};

    private static String[] NOT_NUMBERS = {"-0", "00", "+0",
            "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "+1", "+2", "+3", "+4", "+5", "+6", "+7", "+8", "+9", "+0",
            "+10", "+20", "+30", "+40", "+50", "+60", "+70", "+80", "+90",
            "+0.1", "+0.2", "+0.3", "+0.4", "+0.5", "+0.6", "+0.7", "+0.8", "+0.9",
            "00.1", "00.2", "00.3", "00.4", "00.5", "00.6", "00.7", "00.8", "00.9",
            "0.10", "0.20", "0.30", "0.40", "0.50", "0.60", "0.70", "0.80", "0.90",
            "0.{0}R", "0.{00}R", "00.{0}R", "0.{00}R"};

    @DisplayName("Testing null and empty arguments")
    @ParameterizedTest
    @NullAndEmptySource
    public void isNumberWithNullOrEmptyArgument(String numberString) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> RationalNumberUtil.isNumber(numberString));
        assertEquals(exception.getMessage(), "Null or empty number string");
    }

    @DisplayName("Testing valid numbers as described by @MethodSource(\"numbers\"")
    @ParameterizedTest
    @MethodSource("numbers")
    public void isNumber(String numberString) {
        assertTrue(RationalNumberUtil.isNumber(numberString));
    }

    @DisplayName("Testing valid numbers as described by @MethodSource(\"notNumbers\"")
    @ParameterizedTest
    @MethodSource("notNumbers")
    public void isNotNumber(String numberString) {
        assertFalse(RationalNumberUtil.isNumber(numberString));
    }

    @Test
    public void getRationalNumber() {
    /*    Map<NumberUtil.NumberComponent, String> numberComponents =
                NumberUtil.getNumberComponents("-23.123{456}RE[-47]");
        numberComponents.entrySet().stream().forEach(e -> System.out.println(String.format("%s ---> %s", e.getKey().name(), e.getValue())));
    */

        RationalNumber rationalNumber = RationalNumberUtil.getRationalNumber("0.{142857}R");
        System.out.println(rationalNumber.getNumerator());
        System.out.println(rationalNumber.getDenominator());

    }
    private static Stream<Arguments> numbers() {
        Stream<Arguments> s0 = Stream.of(NUMBERS).map(Arguments::of);
        Stream<Arguments> s1 = Stream.of(NUMBERS).filter(n -> !"0".equals(n)).map(n -> n.concat("E[00]")).map(Arguments::of);
        Stream<Arguments> s2 = Stream.of(NUMBERS).filter(n -> !"0".equals(n)).map(n -> n.concat("E[10]")).map(Arguments::of);
        Stream<Arguments> s3 = Stream.of(NUMBERS).filter(n -> !"0".equals(n)).map(n -> n.concat("E[01]")).map(Arguments::of);
        Stream<Arguments> s4 = Stream.of(NUMBERS).filter(n -> !"0".equals(n)).map(n -> n.concat("E[-00]")).map(Arguments::of);
        Stream<Arguments> s5 = Stream.of(NUMBERS).filter(n -> !"0".equals(n)).map(n -> n.concat("E[-10]")).map(Arguments::of);
        Stream<Arguments> s6 = Stream.of(NUMBERS).filter(n -> !"0".equals(n)).map(n -> n.concat("E[-01]")).map(Arguments::of);

        return Stream.of(s0, s1, s2, s3, s4, s5, s6).flatMap(i -> i);
    }

    private static Stream<Arguments> notNumbers() {
        Stream<Arguments> s0 = Stream.of(NOT_NUMBERS).map(Arguments::of);
        Stream<Arguments> s1 = Stream.of(NUMBERS).map(n -> n.concat("E[0]")).map(Arguments::of);
        Stream<Arguments> s2 = Stream.of(NUMBERS).map(n -> n.concat("E[+0]")).map(Arguments::of);
        Stream<Arguments> s3 = Stream.of(NUMBERS).map(n -> n.concat("E[-0]")).map(Arguments::of);
        Stream<Arguments> s4 = Stream.of(NUMBERS).map(n -> n.concat("E[+00]")).map(Arguments::of);
        Stream<Arguments> s5 = Stream.of(NUMBERS).map(n -> n.concat("E[+10]")).map(Arguments::of);
        Stream<Arguments> s6 = Stream.of(NUMBERS).map(n -> n.concat("E[123]")).map(Arguments::of);
        Stream<Arguments> s7 = Stream.of(NUMBERS).filter(n -> "0".equals(n)).map(n -> n.concat("E[00]")).map(Arguments::of);

        return Stream.of(s0, s1, s2, s3, s4, s5, s6, s7).flatMap(i -> i);
    }

    @Test
    public void doIt() {
        Stack<AbstractMap.SimpleEntry<Integer, Integer>> stack = new Stack<>();
        String pattern = RationalNumberUtil.NUMBER_PATTERN.pattern();
        Character beginToken = new Character('(');
        Character closeToken = new Character(')');
        int groupIndex = 0;
        for (int i = 0; i < pattern.length(); i++) {
            Character character = pattern.charAt(i);

            if (character.equals(beginToken)) {
                stack.push(new AbstractMap.SimpleEntry<>(++groupIndex, i));
            } else if (character.equals(closeToken)) {
                int size = stack.size();
                if (size == 0) {
                    throw new IllegalArgumentException(String.format("Illegal closing brackes at position %d", i));
                }
                AbstractMap.SimpleEntry<Integer, Integer> beginGroup = stack.pop();
                size = stack.size();
                String leadingSpaces = beginGroup.getValue() == 0 ? "" : new String(new char[beginGroup.getValue()]).replace("\0", " ");
                System.out.println(String.format("Size %d. Group %d\t%s", size, beginGroup.getKey(), leadingSpaces + pattern.substring(beginGroup.getValue(), i+1)));
            }
        }
    }
}

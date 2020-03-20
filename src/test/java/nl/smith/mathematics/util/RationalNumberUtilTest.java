package nl.smith.mathematics.util;

import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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
    public void assertIsNumber_usingNullOrEmptyArgument(String numberString) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> RationalNumberUtil.assertIsNumber(numberString));
        assertEquals(exception.getMessage(), "Number string is null, empty or does not represent a number");
    }

    @DisplayName("Testing valid numbers as described by @MethodSource(\"numbers\"")
    @ParameterizedTest
    @MethodSource("numbers")
    void assertIsNumber_legalNumbers(String numberString) {
        RationalNumberUtil.assertIsNumber(numberString);
    }

    @DisplayName("Testing valid numbers as described by @MethodSource(\"notNumbers\"")
    @ParameterizedTest
    @MethodSource("notNumbers")
    void assertIsNumber_notNumbers(String numberString) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> RationalNumberUtil.assertIsNumber(numberString));
        assertEquals(exception.getMessage(), "Number string is null, empty or does not represent a number");
    }

    @DisplayName("Testing retrieval of number components")
    @ParameterizedTest
    @MethodSource("numberComponents")
    void getNumberComponents(String numberString, Map<RationalNumberUtil.NumberComponent, String> expectedComponents) {
        Map<RationalNumberUtil.NumberComponent, String> numberComponents = RationalNumberUtil.getNumberComponents(numberString);

        assertEquals(expectedComponents, numberComponents);
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

    private static Stream<Arguments> numberComponents() {
        return Stream.of(
                Arguments.of("1", Map.of(
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "1")),
                Arguments.of("-1", Map.of(
                        RationalNumberUtil.NumberComponent.SIGN_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "1")),
                Arguments.of("2432902008176640000", Map.of(
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "2432902008176640000")),
                Arguments.of("-1.0{1}R", Map.of(
                        RationalNumberUtil.NumberComponent.SIGN_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "1",
                        RationalNumberUtil.NumberComponent.CONSTANT_FRACTIONAL_PART, "0",
                        RationalNumberUtil.NumberComponent.REPEATING_FRACTIONAL_PART, "1")),
                Arguments.of("-123.456", Map.of(
                        RationalNumberUtil.NumberComponent.SIGN_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "123",
                        RationalNumberUtil.NumberComponent.CONSTANT_FRACTIONAL_PART, "456")),
                Arguments.of("-234.567{8901}R", Map.of(
                        RationalNumberUtil.NumberComponent.SIGN_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "234",
                        RationalNumberUtil.NumberComponent.CONSTANT_FRACTIONAL_PART, "567",
                        RationalNumberUtil.NumberComponent.REPEATING_FRACTIONAL_PART, "8901")),
                Arguments.of("-1E[-13]", Map.of(
                        RationalNumberUtil.NumberComponent.SIGN_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "1",
                        RationalNumberUtil.NumberComponent.SIGN_EXPONENTIAL_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_EXPONENTIAL_PART, "13")),
                Arguments.of("-1E[-03]", Map.of(
                        RationalNumberUtil.NumberComponent.SIGN_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "1",
                        RationalNumberUtil.NumberComponent.SIGN_EXPONENTIAL_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_EXPONENTIAL_PART, "03")),
                Arguments.of("-1E[-00]", Map.of(
                        RationalNumberUtil.NumberComponent.SIGN_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "1",
                        RationalNumberUtil.NumberComponent.SIGN_EXPONENTIAL_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_EXPONENTIAL_PART, "00")),
                Arguments.of("-11E[-00]", Map.of(
                        RationalNumberUtil.NumberComponent.SIGN_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "11",
                        RationalNumberUtil.NumberComponent.SIGN_EXPONENTIAL_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_EXPONENTIAL_PART, "00")),
                Arguments.of("-11.234E[-00]", Map.of(
                        RationalNumberUtil.NumberComponent.SIGN_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "11",
                        RationalNumberUtil.NumberComponent.CONSTANT_FRACTIONAL_PART, "234",
                        RationalNumberUtil.NumberComponent.SIGN_EXPONENTIAL_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_EXPONENTIAL_PART, "00")),
                Arguments.of("-11.234{765}RE[-00]", Map.of(
                        RationalNumberUtil.NumberComponent.SIGN_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_INTEGER_PART, "11",
                        RationalNumberUtil.NumberComponent.CONSTANT_FRACTIONAL_PART, "234",
                        RationalNumberUtil.NumberComponent.REPEATING_FRACTIONAL_PART, "765",
                        RationalNumberUtil.NumberComponent.SIGN_EXPONENTIAL_PART, "-",
                        RationalNumberUtil.NumberComponent.POSITIVE_EXPONENTIAL_PART, "00")));
    }

}

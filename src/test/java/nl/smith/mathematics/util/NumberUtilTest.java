package nl.smith.mathematics.util;

import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilTest {

    @DisplayName("Trying to convert a string to a number")
    @ParameterizedTest
    @MethodSource("numberStrings")
    <T extends Number> void valueOf(String numberString, Class<?> clazz, T expectedNumber, String expectedIllegalArgumentMessage) {
        if (expectedIllegalArgumentMessage == null) {
            assertEquals(expectedNumber, NumberUtil.valueOf(numberString, (Class<? extends Number>) clazz));
        } else {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> NumberUtil.valueOf(numberString, (Class<? extends Number>) clazz));
            assertEquals(expectedIllegalArgumentMessage, exception.getMessage());
        }
    }

    private static Stream<Arguments> numberStrings() {
        return Stream.of(
                Arguments.of("1", byte.class, Byte.valueOf("1"), null),
                Arguments.of("1", Byte.class, Byte.valueOf("1"), null),
                Arguments.of("1", short.class, Short.valueOf("1"), null),
                Arguments.of("1", Short.class, Short.valueOf("1"), null),
                Arguments.of("1", int.class, Integer.valueOf("1"), null),
                Arguments.of("1", Integer.class, Integer.valueOf("1"), null),
                Arguments.of("1", long.class, Long.valueOf("1"), null),
                Arguments.of("1", Long.class, Long.valueOf("1"), null),
                Arguments.of("1", double.class, Double.valueOf("1"), null),
                Arguments.of("1", Double.class, Double.valueOf("1"), null),
                Arguments.of("1", float.class, Float.valueOf("1"), null),
                Arguments.of("1", Float.class, Float.valueOf("1"), null),
                Arguments.of("1", BigInteger.class, new BigInteger("1"), null),
                Arguments.of("1", BigDecimal.class, new BigDecimal("1"), null),
                Arguments.of("1", RationalNumber.class, RationalNumber.valueOf("1"), null),
                Arguments.of("1", String.class, null , String.format("Can not determine number value.%nSpecified class java.lang.String does not extend java.lang.Number"))
        );
    }

}

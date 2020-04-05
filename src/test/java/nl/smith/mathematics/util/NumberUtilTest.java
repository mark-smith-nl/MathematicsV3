package nl.smith.mathematics.util;

import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NumberUtilTest {

    @DisplayName("Trying to convert a string to a number")
    @ParameterizedTest
    @MethodSource("numberStrings")
    void valueOf(String numberString, Class<?> clazz, String expectedIllegalArgumentMessage) {
        if (expectedIllegalArgumentMessage == null){
            NumberUtil.valueOf(numberString, (Class<? extends Number>) clazz);
        } else  {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> NumberUtil.valueOf(numberString, (Class<? extends Number>) clazz));
            assertEquals(expectedIllegalArgumentMessage, exception.getMessage());
        }
    }

    private static Stream<Arguments> numberStrings() {
        return Stream.of(
                Arguments.of("1",byte.class, "Can not determine number value.\nSpecified class byte does not extend java.lang.Number"),
                Arguments.of("1",Byte.class, null),
                Arguments.of("1",short.class, "Can not determine number value.\nSpecified class short does not extend java.lang.Number"),
                Arguments.of("1",Short.class, null),
                Arguments.of("1",int.class, "Can not determine number value.\nSpecified class int does not extend java.lang.Number"),
                Arguments.of("1",Integer.class, null),
                Arguments.of("1",long.class, "Can not determine number value.\nSpecified class long does not extend java.lang.Number"),
                Arguments.of("1",Long.class, null),
                Arguments.of("1",double.class, "Can not determine number value.\nSpecified class double does not extend java.lang.Number"),
                Arguments.of("1",Double.class, null),
                Arguments.of("1",float.class, "Can not determine number value.\nSpecified class float does not extend java.lang.Number"),
                Arguments.of("1",Float.class, null),
                Arguments.of("1", BigInteger.class, null),
                Arguments.of("1", BigDecimal.class, null),
                Arguments.of("1", RationalNumber.class, null)
        );
    }
}
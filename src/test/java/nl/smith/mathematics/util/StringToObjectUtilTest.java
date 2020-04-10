package nl.smith.mathematics.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StringToObjectUtilTest {

    @DisplayName("Trying to retrieve a corresponding wrapper class for a primitve class")
    @ParameterizedTest
    @MethodSource("classes")
    void getPrimitiveClassToWrapperClass(Class clazz, Class expectedClass) {
        assertEquals(expectedClass, StringToObjectUtil.getPrimitiveClassToWrapperClass(clazz));
    }

    private static Stream<Arguments> classes() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(byte.class, Byte.class),
                Arguments.of(short.class, Short.class),
                Arguments.of(int.class, Integer.class),
                Arguments.of(long.class, Long.class),
                Arguments.of(double.class, Double.class),
                Arguments.of(float.class, Float.class),
                Arguments.of(char.class, Character.class),
                Arguments.of(boolean.class, Boolean.class),
                Arguments.of(null, null),
                Arguments.of(void.class, Void.class),
                Arguments.of(String.class, null)
        );
    }
}
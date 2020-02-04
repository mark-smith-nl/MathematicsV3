package nl.smith.mathematics.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Stream;

import static nl.smith.mathematics.service.NumberService.NumberComponent.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NumberServiceTest {

    @Autowired
    private NumberService numberService;

    @Test
    public void numberServiceIsAvailable() {
        assertNotNull(numberService);
    }

    @Test
    public void numberServiceIsScientificNumberWithNullArgument() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> numberService.isScientificNumber(null));
        assertEquals(exception.getMessage(), "isScientificNumber.numberString: Empty number string");
    }

    @Test
    public void numberServiceIsScientificNumberWithEmptyArgument() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> numberService.isScientificNumber(""));
        assertEquals(exception.getMessage(), "isScientificNumber.numberString: Empty number string");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1E4", "2E4", "3E4", "4E4", "5E4", "6E4", "7E4", "8E4", "9E4",
            "-1E4", "-2E4", "-3E4", "-4E4", "-5E4", "-6E4", "-7E4", "-8E4", "-9E4",
            "1E-4", "2E-4", "3E-4", "4E-4", "5E-4", "6E-4", "7E-4", "8E-4", "9E-4",
            "-1E-4", "-2E-4", "-3E-4", "-4E-4", "-5E-4", "-6E-4", "-7E-4", "-8E-4", "-9E-4",
            "1.1E4", "2.1E4", "3.1E4", "4.1E4", "5.1E4", "6.1E4", "7.1E4", "8.1E4", "9.1E4",
            "-1.1E4", "-2.1E4", "-3.1E4", "-4.1E4", "-5.1E4", "-6.1E4", "-7.1E4", "-8.1E4", "-9.1E4"})
    public void numberServiceIsScientificNumber(String numberString) {
        assertTrue(numberService.isScientificNumber(numberString));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0E4", "10E4",
            "+0E4", "+1E4", "+2E4", "+3E4", "+4E4", "+5E4", "+6E4", "+7E4", "+8E4", "+9E4", "+10E4",
            "+0E-4", "+1E-4", "+2E-4", "+3E-4", "+4E-4", "+5E-4", "+6E-4", "+7E-4", "+8E-4", "+9E-4", "+10E-4",
            "1E04", "2E04", "3E04", "4E04", "5E04", "6E04", "7E04", "8E04", "9E04",
            "11E4", "12E4", "13E4", "14E4", "15E4", "16E4", "17E4", "18E4", "19E4",
            "1.0E4", "2.0E4", "3.0E4", "4.0E4", "5.0E4", "6.0E4", "7.0E4", "8.0E4", "9.0E4",
            "1.10E4", "2.10E4", "3.10E4", "4.10E4", "5.10E4", "6.10E4", "7.10E4", "8.10E4", "9.10E4"})
    public void numberServiceIsNotScientificNumber(String numberString) {
        assertFalse(numberService.isScientificNumber(numberString));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1E4", "2E4", "3E4", "4E4", "5E4", "6E4", "7E4", "8E4", "9E4",
            "-1E4", "-2E4", "-3E4", "-4E4", "-5E4", "-6E4", "-7E4", "-8E4", "-9E4",
            "1E-4", "2E-4", "3E-4", "4E-4", "5E-4", "6E-4", "7E-4", "8E-4", "9E-4",
            "-1E-4", "-2E-4", "-3E-4", "-4E-4", "-5E-4", "-6E-4", "-7E-4", "-8E-4", "-9E-4",
            "1.1E4", "2.1E4", "3.1E4", "4.1E4", "5.1E4", "6.1E4", "7.1E4", "8.1E4", "9.1E4",
            "-1.1E4", "-2.1E4", "-3.1E4", "-4.1E4", "-5.1E4", "-6.1E4", "-7.1E4", "-8.1E4", "-9.1E4"})
    //@MethodSource("addFixture")
    public void getNumberComponents(String numberString) {
 //       public void getNumberComponents(String numberString, String sign, String positiveIntegerPart, String constantFractionalPart, String signScientificPart) {
            Map<NumberService.NumberComponent, String> numberComponents = numberService.getNumberComponents(numberString);
        System.out.println(String.format("%s %s:%s %s:%s %s:%s %s:%s %s:%s", numberString,
                SIGN, numberComponents.get(SIGN),
                POSITIVE_INTEGER_PART, numberComponents.get(POSITIVE_INTEGER_PART),
                CONSTANT_FRACTIONAL_PART, numberComponents.get(CONSTANT_FRACTIONAL_PART),
                SIGN_EXPONENTIAL_PART, numberComponents.get(SIGN_EXPONENTIAL_PART),
                POSITIVE_EXPONENTIAL_PART, numberComponents.get(POSITIVE_EXPONENTIAL_PART)));
    }

    private static Stream<Arguments> addFixture() {
        return Stream.of(
                Arguments.of("1E4", null, "1", null , null, null, "4"),
                Arguments.of("2E4", null, "2", null , null, null, "4"),
                Arguments.of("3E4", null, "3", null , null, null, "4"));
    }
}
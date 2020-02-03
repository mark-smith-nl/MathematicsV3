package nl.smith.mathematics.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

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
    @ValueSource(strings = {"1E4", "2.3E+3", "2.3E-3", "2.3E+30", "2.3E-30", "2.03E+30", "2.03E-30",
            "-1E4", "-2.3E+3", "-2.3E-3", "-2.3E+30", "-2.3E-30", "-2.03E+30", "-2.03E-30"})
    public void numberServiceIsScientificNumber(String numberString) {
        assertTrue(numberService.isScientificNumber(numberString));
    }

    @ParameterizedTest
    @ValueSource(strings = {"+1E4", "+2.3E+3", "+2.3E-3", "+2.3E+30", "+2.3E-30", "+2.03E+30", "+2.03E-30",
            "01E4", "02.3E+3", "02.3E-3", "02.3E+30", "02.3E-30", "02.03E+30", "02.03E-30",
            "1E04", "2.3E+03", "2.3E-03", "2.3E+030", "2.3E-030", "2.03E+030", "2.03E-030",
            "1.0E4", "2.30E+3", "2.30E-3", "2.30E+30", "2.30E-30", "2.030E+30", "2.030E-30"})
    public void numberServiceIsNotScientificNumber(String numberString) {
        assertFalse(numberService.isScientificNumber(numberString));
    }
}
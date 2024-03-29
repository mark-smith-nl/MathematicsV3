package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.TextWithoutReservedCharacters;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link TextWithoutReservedCharacters} constraint used on a validated service method argument
 */
@SpringBootTest
class TextWithoutReservedCharactersValidatorTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(TextWithoutReservedCharactersValidatorTest.class);

    private final MethodContainer methodContainer;

    @Autowired
    public TextWithoutReservedCharactersValidatorTest(MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @Test
    void methodArgumentContainsReservedCharacters() {
            ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingTextWithoutReservedCharactersNoCharactersSpecified("1+%2*$a+#4?"));

            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
        Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
        assertTrue(constraintViolationOption.isPresent());
        ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
            assertEquals(String.format("The provided text has reserved characters at position(s): 2, 5, 8, 10.%n" +
                    "Do not use the characters in the set {@, !, #, $, %%, &, =, ?} since they have a special meaning.%n" +
                    "Text:%n" +
                    "1+%%2*$a+#4?"), constraintViolation.getMessage());

    }

    @Test
    void methodArgumentContainsReservedCharacters_specifiedReservedCharacters() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> methodContainer.validatedMethodUsingTextWithoutReservedCharactersCharactersSpecified("1+%2*$a+#4?"));

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        Optional<ConstraintViolation<?>> constraintViolationOption = constraintViolations.stream().findFirst();
        assertTrue(constraintViolationOption.isPresent());
        ConstraintViolation<?> constraintViolation = constraintViolationOption.get();
        assertEquals(format("The provided text has reserved characters at position(s): 2, 10.%n" +
                "Do not use the characters in the set {@, %%, &, ?} since they have a special meaning.%n" +
                "Text:%n" +
                "1+%%2*$a+#4?"), constraintViolation.getMessage());

    }

    @Test
    void methodArgumentDoesNotContainReservedCharacters() {
        methodContainer.validatedMethodUsingTextWithoutReservedCharactersCharactersSpecified("1+2+3");
    }

    @Service
    @Validated
    public static class MethodContainer {

        public void validatedMethodUsingTextWithoutReservedCharactersNoCharactersSpecified(@TextWithoutReservedCharacters() String text) {
            LOGGER.debug("Calling method validatedMethodUsingTextWithoutReservedCharactersNoCharactersSpecified");}

        public void validatedMethodUsingTextWithoutReservedCharactersCharactersSpecified(@TextWithoutReservedCharacters(reservedCharacters = {'@', '%', '&', '?'}) String text) {
            LOGGER.debug("Calling method validatedMethodUsingTextWithoutReservedCharactersCharactersSpecified");}
    }

}

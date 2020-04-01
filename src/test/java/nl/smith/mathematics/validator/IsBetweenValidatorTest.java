package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsBetween;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static org.junit.jupiter.api.Assertions.*;

/** Tests the {@link nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsBetween} constraint used on a validated service method argument */
@SpringBootTest
class IsBetweenValidatorTest {

    private final IsLargerThanValidatorTest.MethodContainer methodContainer;

    @Autowired
    public IsBetweenValidatorTest(IsLargerThanValidatorTest.MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @Service
    @Validated
    public static class MethodContainer {

        public void validatedMethod(@IsBetween(floor = "0", includingFloor = true, ceiling = "100", includingCeiling = true) Object argument) {}
    }
}
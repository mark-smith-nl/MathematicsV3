package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.NullString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class RationalNumberAuxiliaryFunctionsTest {

    // SUT
    private final RationalNumberAuxiliaryFunctions rationalNumberAuxiliaryFunctions;

    @Autowired
    public RationalNumberAuxiliaryFunctionsTest(RationalNumberAuxiliaryFunctions rationalNumberAuxiliaryFunctions) {
        this.rationalNumberAuxiliaryFunctions = rationalNumberAuxiliaryFunctions;
    }

    @DisplayName("Testing faculty using null argument")
    @ParameterizedTest
    @NullSource
    void faculty_usingNullArgument(RationalNumber argument) {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> rationalNumberAuxiliaryFunctions.faculty(argument));
        Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
        assertEquals("No argument specified for faculty method", constraintViolation.getMessage());
    }

    @DisplayName("Testing faculty using @MethodSource(\"facultyArguments\"")
    @ParameterizedTest
    @MethodSource("facultyArguments")
    void faculty(RationalNumber argument, RationalNumber result) {
        assertEquals(result, rationalNumberAuxiliaryFunctions.faculty(argument));
    }

    private static Stream<Arguments> facultyArguments() {
        return Stream.of(
                Arguments.of(new RationalNumber(0), new RationalNumber(1)),
                Arguments.of(new RationalNumber(1), new RationalNumber(1)),
                Arguments.of(new RationalNumber(2), new RationalNumber(2)),
                Arguments.of(new RationalNumber(3), new RationalNumber(6)),
                Arguments.of(new RationalNumber(4), new RationalNumber(24)),
                Arguments.of(new RationalNumber(10), new RationalNumber(3628800)),
                Arguments.of(new RationalNumber(20), new RationalNumber(2432902008176640000l)));
        //  Arguments.of(new RationalNumber(20), RationalNumber.valueOf("2432902008176640000")));
    }
}
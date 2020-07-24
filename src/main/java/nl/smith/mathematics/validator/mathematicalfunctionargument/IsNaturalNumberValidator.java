package nl.smith.mathematics.validator.mathematicalfunctionargument;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.numbertype.ArithmeticOperations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IsNaturalNumberValidator implements ConstraintValidator<IsNaturalNumber, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid = true;

        if (o != null) {
            if (o instanceof Number) {
                Number number = (Number) o;
                Class<? extends Number> clazz = number.getClass();
                if (clazz.equals(BigDecimal.class)) {
                    isValid = ((BigDecimal) o).divideAndRemainder(BigDecimal.ONE)[1].compareTo(BigDecimal.ZERO) == 0;
                } else if (ArithmeticOperations.class.isAssignableFrom(o.getClass())) {
                    isValid = ((ArithmeticOperations<?>) o).isNaturalNumber();
                } else {
                    isValid = clazz.equals(BigInteger.class);
                }
            } else {
                isValid = false;
            }
        }

        return isValid;
    }
}



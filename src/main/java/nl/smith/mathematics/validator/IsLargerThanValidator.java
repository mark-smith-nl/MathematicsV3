package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsSmallerThan;
import nl.smith.mathematics.numbertype.ArithmeticFunctions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IsLargerThanValidator implements ConstraintValidator<IsLargerThan, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid = true;

        //TODO implement
        if (o != null) {
            IsNumberValidator isNumberValidator = new IsNumberValidator();
            if (isNumberValidator.isValid(o, null)) {
                if (o.getClass() == BigInteger.class) {
                } else if (o.getClass() == BigDecimal.class) {
                    isValid = ((BigDecimal) o).divideAndRemainder(BigDecimal.ONE)[1].compareTo(BigDecimal.ZERO) == 0;
                } else if (ArithmeticFunctions.class.isAssignableFrom(o.getClass())) {
                    isValid = ((ArithmeticFunctions) o).isNaturalNumber();
                }
            } else {
                isValid = false;
            }
        }

        return isValid;
    }
}



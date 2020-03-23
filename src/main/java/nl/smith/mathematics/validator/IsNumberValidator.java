package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNumber;
import nl.smith.mathematics.numbertype.ArithmeticFunctions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IsNumberValidator implements ConstraintValidator<IsNumber, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;

        if (o != null) {
            isValid = o.getClass() == BigInteger.class ||
                    o.getClass() == BigDecimal.class ||
                    ArithmeticFunctions.class.isAssignableFrom(o.getClass());
        }

        return isValid;
    }

}

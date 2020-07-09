package nl.smith.mathematics.validator.mathematicalfunctionargument;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.numbertype.ArithmeticFunctions;
import nl.smith.mathematics.util.NumberUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IsNaturalNumberValidator implements ConstraintValidator<IsNaturalNumber, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid = true;

        if (o != null) {
            if (NumberUtil.isNumber(o)) {
                Class<Number> clazz = (Class<Number>) o.getClass();
                if (clazz.equals(BigInteger.class)) {
                } else if (clazz.equals(BigDecimal.class)) {
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



package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan;
import nl.smith.mathematics.numbertype.ArithmeticFunctions;
import nl.smith.mathematics.util.NumberUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IsLargerThanValidator implements ConstraintValidator<IsLargerThan, Object> {

    private String value;

    private boolean includingBoundary;

    @Override
    public void initialize(IsLargerThan constraintAnnotation) {
        value = constraintAnnotation.value();
        includingBoundary = constraintAnnotation.includingBoundary();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid = true;

        if (o != null) {
            if (NumberUtil.isNumber(o)) {
                Class<Number> clazz = (Class<Number>) o.getClass();
                Object value;
                if (clazz.equals(BigInteger.class)) {
                    value = new BigInteger(this.value);
                } else if (clazz.equals(BigDecimal.class)) {
                    value = new BigDecimal(this.value);
                } else if (ArithmeticFunctions.class.isAssignableFrom(o.getClass())) {
                    value = NumberUtil.valueOf(this.value, clazz);
                } else {
                    throw new IllegalStateException("Could not determine minimum value");
                }

                isValid = includingBoundary ? ((Comparable) o).compareTo(value) >= 0 :  ((Comparable) o).compareTo(value) > 0;
            } else {
                isValid = false;
            }
        }

        return isValid;
    }
}



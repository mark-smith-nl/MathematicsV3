package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsSmallerThan;
import nl.smith.mathematics.numbertype.ArithmeticFunctions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IsSmallerThanValidator implements ConstraintValidator<IsSmallerThan, Object> {

    private String value;

    private boolean includingBoundary;

    @Override
    public void initialize(IsSmallerThan constraintAnnotation) {
        value = constraintAnnotation.value();
        includingBoundary = constraintAnnotation.includingBoundary();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid = true;

        if (o != null) {
            if (IsNumberValidator.isValid(o)) {
                Object value;
                if (o.getClass() == BigInteger.class) {
                    value = new BigInteger(this.value);
                } else if (o.getClass() == BigDecimal.class) {
                    value = new BigDecimal(this.value);
                } else if (ArithmeticFunctions.class.isAssignableFrom(o.getClass())) {
                    value = IsNumberValidator.valueOf(this.value, o.getClass());
                }else {
                    throw new IllegalStateException("Could not determine maximum value");
                }

                isValid = includingBoundary ? ((Comparable) o).compareTo(value) <= 0 :  ((Comparable) o).compareTo(value) < 0;
            } else {
                isValid = false;
            }
        }

        return isValid;
    }
}



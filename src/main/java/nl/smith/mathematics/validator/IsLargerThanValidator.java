package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsSmallerThan;
import nl.smith.mathematics.numbertype.ArithmeticFunctions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IsLargerThanValidator implements ConstraintValidator<IsLargerThan, Object> {

    private String floor;

    private boolean includingFloor;

    @Override
    public void initialize(IsLargerThan constraintAnnotation) {
        floor = constraintAnnotation.floor();
        includingFloor = constraintAnnotation.includingFloor();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid = true;

        if (o != null) {
            if (IsNumberValidator.isValid(o)) {
                Object floor = null;
                if (o.getClass() == BigInteger.class) {
                    floor = new BigInteger(this.floor);
                } else if (o.getClass() == BigDecimal.class) {
                    floor = new BigDecimal(this.floor);
                } else if (ArithmeticFunctions.class.isAssignableFrom(o.getClass())) {
                    floor = IsNumberValidator.valueOf(this.floor, o.getClass());
                }

                isValid = includingFloor ? ((Comparable) o).compareTo(floor) >= 0 :  ((Comparable) o).compareTo(floor) > 0;
            } else {
                isValid = false;
            }
        }

        return isValid;
    }
}



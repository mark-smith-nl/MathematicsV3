package nl.smith.mathematics.validator.mathematicalfunctionargument;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsSmallerThan;
import nl.smith.mathematics.util.NumberUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
            if (o instanceof Number) {
                Number number = (Number) o;
                Number thresholdValue = NumberUtil.valueOf(this.value, number.getClass());
                isValid = includingBoundary ? ((Comparable) o).compareTo(thresholdValue) <= 0 :  ((Comparable) o).compareTo(thresholdValue) < 0;
            } else {
                isValid = false;
            }
        }

        return isValid;
    }
}



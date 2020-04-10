package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan;
import nl.smith.mathematics.util.NumberUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
                Number thresholdValue = NumberUtil.valueOf(this.value, (Class<Number>) o.getClass());
                isValid = includingBoundary ? ((Comparable) o).compareTo(thresholdValue) >= 0 :  ((Comparable) o).compareTo(thresholdValue) > 0;
            } else {
                isValid = false;
            }
        }

        return isValid;
    }
}


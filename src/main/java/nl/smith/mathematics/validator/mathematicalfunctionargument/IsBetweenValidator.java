package nl.smith.mathematics.validator.mathematicalfunctionargument;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsBetween;
import nl.smith.mathematics.util.NumberUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsBetweenValidator implements ConstraintValidator<IsBetween, Object> {

    private String floorAsString;

    private boolean includingFloor;

    private String ceilingAsString;

    private boolean includingCeiling;

    @Override
    public void initialize(IsBetween constraintAnnotation) {
        this.floorAsString = constraintAnnotation.floor();
        this.includingFloor = constraintAnnotation.includingFloor();
        this.ceilingAsString = constraintAnnotation.ceiling();
        this.includingCeiling = constraintAnnotation.includingCeiling();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;

        if (o != null) {
            if (NumberUtil.isNumber(o)) {
                Number floor = NumberUtil.valueOf(this.floorAsString, (Class<Number>) o.getClass());
                Number ceiling = NumberUtil.valueOf(this.ceilingAsString, (Class<Number>) o.getClass());
                isValid = (includingFloor ? ((Comparable) o).compareTo(floor) >= 0 :  ((Comparable) o).compareTo(floor) > 0) &&
                (includingCeiling ? ((Comparable) o).compareTo(ceiling) <= 0 :  ((Comparable) o).compareTo(ceiling) < 0);
            } else {
                isValid = false;
            }
        }

        return isValid;
    }

}

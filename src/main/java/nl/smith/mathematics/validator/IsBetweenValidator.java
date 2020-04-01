package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsBetween;
import nl.smith.mathematics.numbertype.ArithmeticFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IsBetweenValidator implements ConstraintValidator<IsBetween, Object> {

    private static final String FACTORY_METHOD_NAME = "valueOf";

    private String floor;

    private boolean includingFloor;

    private String ceiling;

    private boolean includingCeiling;


    @Override
    public void initialize(IsBetween constraintAnnotation) {
        this.floor = constraintAnnotation.floor();
        this.includingFloor = constraintAnnotation.includingFloor();
        this.ceiling = constraintAnnotation.ceiling();
        this.includingCeiling = constraintAnnotation.includingCeiling();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;

        Object floor;
        Object ceiling;

        if (o != null) {
            if (IsNumberValidator.isValid(o)) {
                if (o.getClass() == BigInteger.class) {
                    floor = new BigInteger(this.floor);
                    ceiling = new BigInteger(this.floor);
                } else if (o.getClass() == BigDecimal.class) {
                    floor = new BigDecimal(this.floor);
                    ceiling = new BigDecimal(this.ceiling);
                } else if (ArithmeticFunctions.class.isAssignableFrom(o.getClass())) {
                    floor = IsNumberValidator.valueOf(this.floor, o.getClass());
                    ceiling = IsNumberValidator.valueOf(this.ceiling, o.getClass());
                } else {
                    throw new IllegalStateException("Could not determine floor and ceiling values");
                }
                isValid = (includingFloor ? ((Comparable) o).compareTo(floor) >= 0 :  ((Comparable) o).compareTo(floor) > 0) &&
                (includingCeiling ? ((Comparable) o).compareTo(ceiling) <= 0 :  ((Comparable) o).compareTo(ceiling) < 0);
            } else {
                isValid = false;
            }
        }

        return isValid;
    }

}

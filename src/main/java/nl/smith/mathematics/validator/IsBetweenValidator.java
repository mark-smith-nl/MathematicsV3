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

    private String ceiling;

    private String floor;

    private boolean includingCeiling;

    private boolean includingFloor;

    @Override
    public void initialize(IsBetween constraintAnnotation) {
        this.ceiling = constraintAnnotation.ceiling();
        this.floor = constraintAnnotation.floor();
        this.includingCeiling = constraintAnnotation.includingCeiling();
        this.includingFloor = constraintAnnotation.includingFloor();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;

        Object ceiling;
        Object floor;

        if (o != null) {
            if (o.getClass() == BigInteger.class) {
                isValid = isBeteen(o, new BigInteger(this.ceiling), new BigInteger(this.floor));
            } else if (o.getClass() == BigDecimal.class) {
                isValid = isBeteen(o, new BigDecimal(this.ceiling), new BigDecimal(this.floor));
            } else if (ArithmeticFunctions.class.isAssignableFrom(o.getClass())) {
                try {
                    Method valueOf = o.getClass().getDeclaredMethod(FACTORY_METHOD_NAME, new Class<?>[]{String.class});
                    isValid = isBeteen(o, valueOf.invoke(null, this.ceiling), valueOf.invoke(null, this.floor));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(String.format("Invokable public static method %s.%s(String) not found.", o.getClass(), FACTORY_METHOD_NAME));
                }
            } else {
                isValid = false;
            }

        }

        return isValid;
    }

    private boolean isBeteen(Object o, Object ceiling, Object floor) {
        if (includingFloor) {
            if (includingCeiling) {
                return (((Comparable) o).compareTo(floor) > -1) && (((Comparable) o).compareTo(ceiling) < 1);
            } else {
                return (((Comparable) o).compareTo(floor) > -1) && (((Comparable) o).compareTo(ceiling) < 0);
            }
        } else {
            if (includingCeiling) {
                return (((Comparable) o).compareTo(floor) > 0) && (((Comparable) o).compareTo(ceiling) < 1);
            } else {
                return (((Comparable) o).compareTo(floor) > 0) && (((Comparable) o).compareTo(ceiling) < 0);
            }
        }
    }
}

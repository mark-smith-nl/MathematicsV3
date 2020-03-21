package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.IsNaturalNumber;
import nl.smith.mathematics.annotation.IsPublicInstanceMethod;
import nl.smith.mathematics.numbertype.ArithmeticFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IsNaturalNumberValidator implements ConstraintValidator<IsNaturalNumber, Object> {

    private Class<?> numberClass;

    @Override
    public void initialize(IsNaturalNumber constraintAnnotation) {
        numberClass = constraintAnnotation.numberClass();

        isValidNumberClass(numberClass);
    }

    private void isValidNumberClass(Class<?> numberClass) {
        if (numberClass == null || BigDecimal.class != numberClass || !ArithmeticFunctions.class.isAssignableFrom(numberClass)) {
            throw new IllegalStateException(String.format("The annotation %s can only be placed om method parameters of type %s or %s", BigDecimal.class.getCanonicalName(), ArithmeticFunctions.class.getCanonicalName()));
        }
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (numberClass == BigDecimal.class) {
            BigDecimal number = (BigDecimal) o;
            return number.divideAndRemainder(BigDecimal.ONE)[1].compareTo(BigDecimal.ZERO) == 0;
        }

      //  ArithmeticFunctions number = (ArithmeticFunctions) o;
      //  return number.divideAndRemainder(RationalNumber.ONE)[1].compareTo(BigDecimal.ZERO) == 0;

        return false;
    }

    public static void main(String[] args) {
        BigDecimal a = BigDecimal.valueOf(-234.0000000);
        System.out.println(a.divideAndRemainder(BigDecimal.ONE)[1].compareTo(BigDecimal.ZERO) == 0);
    }
}

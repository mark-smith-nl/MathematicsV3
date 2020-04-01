package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNumber;
import nl.smith.mathematics.numbertype.ArithmeticFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IsNumberValidator implements ConstraintValidator<IsNumber, Object> {

    private static final String NUMBER_FACTORY_METHOD_NAME = "valueOf";

    private static final Class<?>[] NUMBER_FACTORY_METHOD_ARGUMENT_TYPES = new Class<?>[]{String.class};


    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(o);
    }

    public static boolean isValid(Object o) {
        boolean isValid = true;

        if (o != null) {
            isValid = BigInteger.class.equals(o.getClass()) ||
                    BigDecimal.class.equals(o.getClass()) ||
                    ArithmeticFunctions.class.isAssignableFrom(o.getClass());
        }

        return isValid;
    }

    public static <T> T valueOf(String stringNumber, Class<T> clazz) {
        if (stringNumber == null ||
                stringNumber.isEmpty() ||
                !(BigInteger.class.equals(clazz) ||
                    BigDecimal.class.equals(clazz) ||
                    ArithmeticFunctions.class.isAssignableFrom(clazz))) {
            throw new IllegalArgumentException("Can not determine number value");
        }

        try {
            Method valueOf = clazz.getDeclaredMethod(NUMBER_FACTORY_METHOD_NAME, NUMBER_FACTORY_METHOD_ARGUMENT_TYPES);
            return (T) valueOf.invoke(null, stringNumber);
        } catch (Exception e) {
            String arguments = Stream.of(NUMBER_FACTORY_METHOD_ARGUMENT_TYPES).map(Class::getCanonicalName).collect(Collectors.joining(", "));
            throw new IllegalStateException(String.format("Can not invoke method %s.%s(%s)", clazz.getCanonicalName(), NUMBER_FACTORY_METHOD_NAME, arguments));
        }
    }

}

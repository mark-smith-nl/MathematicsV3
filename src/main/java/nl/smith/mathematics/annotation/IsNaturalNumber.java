package nl.smith.mathematics.annotation;

import nl.smith.mathematics.validator.IsNaturalNumberValidator;
import nl.smith.mathematics.validator.IsPublicInstanceMethodValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsNaturalNumberValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNaturalNumber {

  String message() default "Change message: Method ${validatedValue.getDeclaringClass().getCanonicalName()}.${validatedValue.getName()}(...) is not a public instance method";

  Class<?> numberClass();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

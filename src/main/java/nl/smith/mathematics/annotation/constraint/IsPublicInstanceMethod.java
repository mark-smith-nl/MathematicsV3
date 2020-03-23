package nl.smith.mathematics.annotation.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import nl.smith.mathematics.validator.IsPublicInstanceMethodValidator;

@Documented
@Constraint(validatedBy = IsPublicInstanceMethodValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsPublicInstanceMethod {

  String message() default "Method ${validatedValue.getDeclaringClass().getCanonicalName()}.${validatedValue.getName()}(...) is not a public instance method";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

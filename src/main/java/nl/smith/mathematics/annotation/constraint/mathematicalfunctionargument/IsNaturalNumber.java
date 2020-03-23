package nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument;

import nl.smith.mathematics.validator.IsNaturalNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsNaturalNumberValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNaturalNumber {

  String message() default "Value is not an integer number: '${validatedValue}'";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

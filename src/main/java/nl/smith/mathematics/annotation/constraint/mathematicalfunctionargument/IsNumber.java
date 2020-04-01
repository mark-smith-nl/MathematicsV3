package nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument;

import nl.smith.mathematics.validator.IsNaturalNumberValidator;
import nl.smith.mathematics.validator.IsNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsNumberValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNumber {

  String message() default "${isNumber.not.true}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

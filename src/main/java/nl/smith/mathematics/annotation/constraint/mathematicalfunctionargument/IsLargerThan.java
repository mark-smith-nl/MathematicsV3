package nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument;

import nl.smith.mathematics.validator.IsBetweenValidator;
import nl.smith.mathematics.validator.IsLargerThanValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsLargerThanValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsLargerThan {

  String message() default "Value is not a number that ....: '${validatedValue}'";

  String floor();

  boolean includingFloor() default false;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

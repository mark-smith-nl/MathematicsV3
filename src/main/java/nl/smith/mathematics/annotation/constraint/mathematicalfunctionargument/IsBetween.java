package nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument;

import nl.smith.mathematics.validator.IsBetweenValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsBetweenValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsBetween {

  String message() default "Value is not a number that ....: '${validatedValue}'";

  String ceiling();

  String floor();

  boolean includingCeiling() default false;

  boolean includingFloor() default false;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

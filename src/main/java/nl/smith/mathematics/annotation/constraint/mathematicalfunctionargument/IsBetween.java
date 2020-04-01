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

  String message() default "{isBetween.not.true}";

   /** Value is not an int since it is not bound to the domain {@link java.lang.Integer#MIN_VALUE} {@link java.lang.Integer#MAX_VALUE} */
  String floor();

  boolean includingFloor() default false;

  /** Value is not an int since it is not bound to the domain {@link java.lang.Integer#MIN_VALUE} {@link java.lang.Integer#MAX_VALUE} */
  String ceiling();

  boolean includingCeiling() default false;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

package nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument;

import nl.smith.mathematics.validator.IsLargerThanValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsLargerThanValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsLargerThan {

  String message() default "{isLargerThan.not.true}";

  /** Value is not an int, double or other numeric primitive datatype since it is not bound to the domain {@link java.lang.Integer#MIN_VALUE} {@link java.lang.Integer#MAX_VALUE} */
  String value();

  boolean includingBoundary() default false;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

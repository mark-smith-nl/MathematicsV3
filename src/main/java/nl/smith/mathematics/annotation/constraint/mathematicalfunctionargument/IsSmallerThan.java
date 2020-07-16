package nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument;

import nl.smith.mathematics.validator.mathematicalfunctionargument.IsSmallerThanValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsSmallerThanValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsSmallerThan {

  String message() default "{isSmallerThan.not.true}";

  /** Value is not of type {@link Number}
   * The value is a string which can be converted to a value of type {@link Number}
   * The actual type is that of the annotated parameter.*/
  String value();

  boolean includingBoundary() default false;

  @SuppressWarnings("unused")
  Class<?>[] groups() default {};

  @SuppressWarnings("unused")
  Class<? extends Payload>[] payload() default {};

}

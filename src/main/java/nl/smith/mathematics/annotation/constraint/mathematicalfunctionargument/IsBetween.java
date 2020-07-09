package nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument;

import nl.smith.mathematics.validator.mathematicalfunctionargument.IsBetweenValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsBetweenValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsBetween {

  String message() default "{isBetween.not.true}";

  /** Value is not of type {@link Number}
   * The value is a string which can be converted to a value of type {@link Number}
   * The actual type is that of the annotated parameter.*/
  String floor();

  boolean includingFloor() default false;

  /** Value is not of type {@link Number}
   * The value is a string which can be converted to a value of type {@link Number}
   * The actual type is that of the annotated parameter.*/
  String ceiling();

  boolean includingCeiling() default false;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

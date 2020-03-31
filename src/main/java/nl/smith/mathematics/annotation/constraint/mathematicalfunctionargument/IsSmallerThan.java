package nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument;

import nl.smith.mathematics.validator.IsBetweenValidator;
import nl.smith.mathematics.validator.IsSmallerThanValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsSmallerThanValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsSmallerThan {

  String message() default "Value is not a number that ....: '${validatedValue}'";

  String ceiling();

  boolean includingCeiling() default false;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

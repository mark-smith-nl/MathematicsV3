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

  String message() default "Value ${validatedValue}${validatedValue == null ? '' : '('.concat(validatedValue.getClass().getCanonicalName()).concat(')')} is not a number or the assumption {floor} <${includingFloor ? '=' : ''} ${'('.concat(validatedValue).concat(')')} is not true";

  String floor();

  boolean includingFloor() default false;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

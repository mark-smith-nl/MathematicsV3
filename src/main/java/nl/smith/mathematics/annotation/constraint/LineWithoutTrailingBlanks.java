package nl.smith.mathematics.annotation.constraint;

import nl.smith.mathematics.validator.TextValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TextValidation.LineWithoutTrailingBlanksValidator.class)
@Repeatable(LineWithoutTrailingBlanks.List.class)
@Target({ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LineWithoutTrailingBlanks {

  String message() default "The provided text is not a line. It contains a new line character and/or contains trailing white space characters.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    LineWithoutTrailingBlanks[] value();
  }
}

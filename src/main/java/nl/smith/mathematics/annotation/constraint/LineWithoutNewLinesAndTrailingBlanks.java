package nl.smith.mathematics.annotation.constraint;

import nl.smith.mathematics.validator.TextValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TextValidation.LineWithoutNewLinesAndTrailingBlanksValidator.class)
@Repeatable(LineWithoutNewLinesAndTrailingBlanks.List.class)
@Target({ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LineWithoutNewLinesAndTrailingBlanks {

  String message() default "The provided text is not a line. It contains a new line character and/or contains trailing white space characters.";

  @SuppressWarnings("unused")
  Class<?>[] groups() default {};

  @SuppressWarnings("unused")
  Class<? extends Payload>[] payload() default {};

  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {
    LineWithoutNewLinesAndTrailingBlanks[] value();
  }
}

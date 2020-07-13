package nl.smith.mathematics.annotation.constraint;

import nl.smith.mathematics.validator.TextValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TextValidation.TextWithoutLinesWithTrailingBlanksValidator.class)
@Repeatable(TextWithoutLinesWithTrailingBlanks.List.class)
@Target({ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TextWithoutLinesWithTrailingBlanks {

  String message() default "";

  @SuppressWarnings("unused")
  Class<?>[] groups() default {};

  @SuppressWarnings("unused")
  Class<? extends Payload>[] payload() default {};

  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {
    TextWithoutLinesWithTrailingBlanks[] value();
  }
}

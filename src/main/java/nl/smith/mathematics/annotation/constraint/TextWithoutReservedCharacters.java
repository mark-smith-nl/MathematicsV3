package nl.smith.mathematics.annotation.constraint;

import nl.smith.mathematics.validator.TextValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TextValidation.TextWithoutReservedCharactersValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TextWithoutReservedCharacters {

  String message() default "";

  char[] reservedCharacters() default {'$', '#', '?', '@', '&', '%', '!', '='};

  @SuppressWarnings("unused")
  Class<?>[] groups() default {};

  @SuppressWarnings("unused")
  Class<? extends Payload>[] payload() default {};

}

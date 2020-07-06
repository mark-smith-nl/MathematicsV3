package nl.smith.mathematics.annotation.constraint;

import nl.smith.mathematics.validator.ConsistentTextAnnotationParametersValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConsistentTextAnnotationParametersValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsistentTextAnnotationParameters {

  String message() default "The provided text string contains empty lines or none empty lines with trailing white space characters or positions are not well defined.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.ConsistentTextAnnotationParameters;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ConsistentTextAnnotationParametersValidator implements ConstraintValidator<ConsistentTextAnnotationParameters, Object[]> {

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid = true;

        if (value.length != 2) {
            throw new IllegalStateException("Method should have at exactly two parameters to be validated.");

        }

        if (value[0] != null && value[1] != null) {
            if (!(value[0] instanceof String)) {
                throw new IllegalStateException("First method argument should be of type String.");
            }
            String text = (String) value[0];

            int[] position;
            if ((value[1] instanceof int[])) {
                position = (int[]) value[1];
            } else if (value[1] instanceof Collection<?>) {
                position = ((Collection<Integer>) value[1]).stream().mapToInt(Number::intValue).toArray();
            } else {
                throw new IllegalStateException("Second method argument should be of type int[] or Coleection<Integer>.");
            }

            isValid = text.equals(getValidText(text));

            if (isValid) {
                isValid = position.length == Arrays.stream(position).boxed()
                        .filter(p -> p >= 0)
                        .filter(p -> p < text.length())
                        .collect(Collectors.toSet()).size();
            }
        }

        return isValid;
    }

    /**
     * Protected for test purposes.
     * Removes empty lines and lines with trailing white space characters.
     * Is the text ends with a newline the result also ends with a newline.
     */
    protected static String getValidText(String text) {
        return Arrays.asList(text.split("\n")).stream()
                .filter(s -> !s.isEmpty()) // Empty lines are not allowed
                .filter(s -> !(s.matches(".*\\s"))) // Lines with trailing white space characters are not allowed
                .collect(Collectors.joining("\n")) + (text.endsWith("\n") ? "\n" : "");
    }

}



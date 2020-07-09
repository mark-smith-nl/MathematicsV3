package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.CharacterPositionsInRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class CharacterPositionsInRangeValidator implements ConstraintValidator<CharacterPositionsInRange, Object[]> {

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid = true;

        if (value.length != 2) {
            throw new IllegalStateException("Method should have at exactly two parameters to be validated.");

        }

        // Null values result in a postive validation.
        // Null values should be checked with @NotNull constraint annotations placed on the method parameters.
        if (value[0] != null && value[1] != null) {
            if (!(value[0] instanceof String)) {
                // An exception is thrown. A constraint violation is not raised. Inproper use of annotations.
                throw new IllegalStateException("First method argument should be of type String.");
            }
            String text = (String) value[0];

            Collection<Integer> positions;
            if ((value[1] instanceof int[])) {
                positions = IntStream.of((int[]) value[1]).boxed().collect(Collectors.toList());
            } else if (value[1] instanceof Set<?>) {
                positions = (Set<Integer>) value[1];
            } else {
                // An exception is thrown. A constraint violation is not raised. Inproper use of annotations.
                throw new IllegalStateException("Second method argument should be of type int[] or Set<Integer>.");
            }

            if (!positions.isEmpty() && positions.stream().filter(p -> p < 0).collect(Collectors.toSet()).isEmpty()) {
                // Positions are specified that are not negative.
                Set<Integer> outOfRangePositions = positions.stream()
                        .filter(p -> p >= text.length()) // Remove positions that are out of range.
                        .collect(Collectors.toSet());

                if (!outOfRangePositions.isEmpty()) {
                    isValid = false;
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                            String.format("Supplied positions contain values(%s) larger than or equal to the size of the provided string (%d).",
                                    positions.stream().sorted().map(String::valueOf).collect(Collectors.joining(", ")),
                                    text.length()))
                            .addParameterNode(1).addConstraintViolation();
                }
            }
        }

        return isValid;
    }

    /**
     * Protected for test purposes.
     * Removes empty lines and lines with trailing white space characters.
     * If the text ends with a newline the result also ends with a newline.
     */
    protected static String getValidText(String text) {
        return Arrays.asList(text.split("\n")).stream()
                .filter(s -> !s.isEmpty()) // Empty lines are not allowed
                .filter(s -> !(s.matches(".*\\s"))) // Lines with trailing white space characters are not allowed
                .collect(Collectors.joining("\n")) + (text.endsWith("\n") ? "\n" : "");
    }

}



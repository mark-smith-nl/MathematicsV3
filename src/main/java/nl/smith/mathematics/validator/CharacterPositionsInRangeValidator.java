package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.CharacterPositionsInRange;
import nl.smith.mathematics.util.ObjectWrapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
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

        // Null values result in a positive validation.
        // Null values should be checked with @NotNull constraint annotations placed on the method parameters.
        if (value[0] != null && value[1] != null) {
            ObjectWrapper<Integer> maximumPosition = new ObjectWrapper<>(-1);

            if (value[0] instanceof String) {
                maximumPosition.setValue(((String) value[0]).length() - 1);
            } else if (value[0] instanceof List && !((List<?>) value[0]).isEmpty() && ((List<?>) value[0]).get(0) instanceof String) {
                List<String> lines = (List<String>) value[0];
                lines.forEach(l -> maximumPosition.setValue(maximumPosition.getValue() + l.length()));
            } else {
                // An exception is thrown. A constraint violation is not raised. Inproper use of annotations.
                throw new IllegalStateException("First method argument should be of type String or List<string>.");
            }

            Collection<Integer> positions;
            if ((value[1] instanceof int[])) {
                positions = IntStream.of((int[]) value[1]).boxed().collect(Collectors.toList());
            } else if (value[1] instanceof Set<?>) {
                positions = (Set<Integer>) value[1];
            } else {
                // An exception is thrown. A constraint violation is not raised. Inproper use of annotations.
                throw new IllegalStateException("Second method argument should be of type int[] or Set<Integer>.");
            }

            if (!positions.isEmpty()) {
                Set<Integer> outOfRangePositions = positions.stream()
                        .filter(p -> p > maximumPosition.getValue()) // Remove positions that are out of range.
                        .collect(Collectors.toSet());

                if (!outOfRangePositions.isEmpty()) {
                    isValid = false;
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                            String.format("Supplied positions contain values(%s) larger than or equal to the size of the provided string (%d).",
                                    positions.stream().sorted().map(String::valueOf).collect(Collectors.joining(", ")),
                                    maximumPosition.getValue() + 1))
                            .addParameterNode(1).addConstraintViolation();
                }
            }
        }

        return isValid;
    }

}



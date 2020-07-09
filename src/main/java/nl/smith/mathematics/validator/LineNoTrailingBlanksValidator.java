package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.LineNoTrailingBlanks;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LineNoTrailingBlanksValidator implements ConstraintValidator<LineNoTrailingBlanks, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid = true;

        if (s != null && !s.isEmpty()) {
            isValid = !(s.contains("\n"));

            if (isValid) {
                isValid = !s.matches(".*\\s");
            }
        }

        return isValid;
    }
}

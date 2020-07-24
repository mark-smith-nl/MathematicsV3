package nl.smith.mathematics.validator.mathematicalfunctionargument;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsNumberValidator implements ConstraintValidator<IsNumber, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;

        if (o != null && !(o instanceof Number)) {
            isValid = false;
        }

        return isValid;
    }

}
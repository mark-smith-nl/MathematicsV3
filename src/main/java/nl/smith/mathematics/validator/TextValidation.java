package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.LineWithoutNewLinesAndTrailingBlanks;
import nl.smith.mathematics.annotation.constraint.TextWithoutLinesWithTrailingBlanks;
import nl.smith.mathematics.annotation.constraint.TextWithoutReservedCharacters;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class TextValidation {

    public static List<Integer> getPositionsIllegalLineEndings(String s) {
        List<Integer> positions = new ArrayList<>();

        if (s != null && !s.isEmpty()) {
            Pattern pattern = Pattern.compile("(\\s)+\n|(\\s)+$");
            Matcher matcher = pattern.matcher(s);

            while (matcher.find()) {
                positions.add(matcher.start());
            }
        }

        return positions;
    }

    public static class LineWithoutNewLinesAndTrailingBlanksValidator implements ConstraintValidator<LineWithoutNewLinesAndTrailingBlanks, String> {
        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

            boolean isValid = true;

            if (s != null && !s.isEmpty() && !s.matches("\\s*")) {
                isValid = !(s.matches("\n"));

                if (isValid) {
                    isValid = !s.contains("\n") && !s.matches(".*\\s");
                }
            }

            return isValid;
        }

    }

    public static class TextWithoutLinesWithTrailingBlanksValidator implements ConstraintValidator<TextWithoutLinesWithTrailingBlanks, String> {

        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
            boolean isValid = true;

            List<Integer> positionsIllegalLineEndings = getPositionsIllegalLineEndings(s);
            if (!positionsIllegalLineEndings.isEmpty()) {

                isValid = false;
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        format("The provided text has lines with trailing whitespace characters at position(s): %s.", positionsIllegalLineEndings.stream().map(String::valueOf).collect(Collectors.joining(", "))))
                        .addConstraintViolation();
            }
            return isValid;
        }
    }

    public static class TextWithoutReservedCharactersValidator implements ConstraintValidator<TextWithoutReservedCharacters, String> {

        private final Set<Character> reservedCharacters = new HashSet<>();

        @Override
        public void initialize(TextWithoutReservedCharacters constraintAnnotation) {
            for (char predefinedCharacter : constraintAnnotation.reservedCharacters()) {
                reservedCharacters.add(predefinedCharacter);
            }

        }

        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
            boolean isValid = true;

            if (s != null) {
                Set<Integer> positions = new HashSet<>();

                for (int position = 0; position < s.length(); position++) {
                    if (reservedCharacters.contains(s.charAt(position))) {
                        positions.add(position);
                    }
                }

                if (!positions.isEmpty()) {
                    isValid = false;

                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                            format("The provided text has reserved characters at position(s): %s.%nDo not use the characters in the set {%s} since they have a special meaning.%nText:%n%s",
                                    positions.stream().map(String::valueOf).collect(Collectors.joining(", ")),
                                    reservedCharacters.stream().map(String::valueOf).collect(Collectors.joining(", ")),
                                    s))
                            .addConstraintViolation();
                }
            }

            return isValid;
        }
    }

}

package nl.smith.mathematics.validator;

import nl.smith.mathematics.annotation.constraint.LineWithoutTrailingBlanks;
import nl.smith.mathematics.annotation.constraint.TextWithoutLinesWithTrailingBlanks;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextValidation {

    public static boolean isValidText(String s) {
        return s != null && !s.isEmpty() && s.matches("(.*\\S\n)*(.*\\S\n?)");
    }

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

    public static class LineWithoutTrailingBlanksValidator implements ConstraintValidator<LineWithoutTrailingBlanks, String> {
        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

            boolean isValid = true;

            if (s != null && !s.isEmpty() && !s.matches("\\s*") ) {
                isValid = !(s.matches("\n"));

                if (isValid) {
                    isValid = !s.matches(".*\\s");
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
                        String.format("The provided text has lines with trailing whitespace characters at position(s): %s.", positionsIllegalLineEndings.stream().map(String::valueOf).collect(Collectors.joining(", "))))
                        .addConstraintViolation();
                System.out.println();
            }
            return isValid;
        }
    }

    public static void main(String[] args) {
        String text = "Mijn naam iss \nIk woon in     \t\naaa        ";
        //             012345678901234 5678901234567890 1 2345
        Pattern pattern = Pattern.compile("(\\s)+\n|(\\s)+$");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            System.out.println(matcher.start());
        }

    }
}

package nl.smith.mathematics.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Validated
public class NumberService {
    public static enum NumberComponent{
        SIGN(1),
        POSITIVE_INTEGER_PART(2),
        CONSTANT_FRACTIONAL_PART(3),
        //REPEATING_FRACTIONAL_PART(4),
        SIGN_EXPONENTIAL_PART(4),
        POSITIVE_EXPONENTIAL_PART(5);

        private int position;

        NumberComponent(int position) {
            this.position = position;
        }

        private static Map<NumberComponent, String> getNumberComponents(Matcher matcher) {
            Map<NumberComponent, String> numberComponents = new HashMap<>();
            if (matcher.matches()) {
                for (NumberComponent numberComponent : NumberComponent.values()) {
                    numberComponents.put(numberComponent, matcher.group(numberComponent.position));
                }
            }

            return numberComponents;
        }
    };

    private static final Pattern SCIENTIFIC_NUMBER_PATTERN = Pattern.compile("(\\-)?([1-9])(\\.(\\d*[1-9]))?E(\\-)?([1-9]\\d*)");

    public boolean isScientificNumber(@NotEmpty(message = "Empty number string") String numberString) {
        return SCIENTIFIC_NUMBER_PATTERN.matcher(numberString).matches();
    }

    public @NotNull(message = "String can not be converted to a number") Map<NumberComponent, String> getNumberComponents(@NotEmpty(message = "Empty number string") String numberString) {
        return NumberComponent.getNumberComponents(SCIENTIFIC_NUMBER_PATTERN.matcher(numberString));
    }
}

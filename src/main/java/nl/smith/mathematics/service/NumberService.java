package nl.smith.mathematics.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Validated
public class NumberService {
    public static enum NumberComponent{
        INTEGER_PART,
        CONSTANT_FRACTIONAL_PART,
        REPEATING_FRACTIONAL_PART,
        SCIENTIFIC_PART;
    };

    private static final Pattern SCIENTIFIC_NUMBER_PATTERN = Pattern.compile("(\\-?)([1-9])(\\.\\d*[1-9])?E([\\+\\-]?[1-9]\\d*)");

    public boolean isScientificNumber(@NotEmpty(message = "Empty number string") String numberString) {
        return SCIENTIFIC_NUMBER_PATTERN.matcher(numberString).matches();
    }

    public Map<NumberComponent, String> getNumberComponents(@NotEmpty(message = "Empty number string") String numberString) {
        return null;
    }
}

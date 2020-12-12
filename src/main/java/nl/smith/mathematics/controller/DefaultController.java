package nl.smith.mathematics.controller;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberAuxiliaryFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberGoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberLogarithmicFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

import static nl.smith.mathematics.configuration.constant.NumberConstant.IntegerValueOf.Scale;
import static nl.smith.mathematics.configuration.constant.NumberConstant.IntegerValueOf.TaylorDegreeOfPolynom;

//@Controller
@Validated
@RequestMapping(DefaultController.MAPPING_FUNCTION)
public class DefaultController {

    public static final String MAPPING_FUNCTION = "/function";
    public static final String MAPPING_SIN = "sinus";
    public static final String MAPPING_EXP = "exp";
    public static final String MAPPING_FACULTY = "faculty";
    public static final String MAPPING_RATIONAL_VALUE_FROM_NUMERATOR_DENOMINATOR = "rationalValueOfUsingNumeratorDenominator";
    public static final String MAPPING_RATIONAL_VALUE_FROM_STRING = "rationalValueOfUsingString";

    private final RationalNumberGoniometricFunctions goniometricFunctions;
    private final RationalNumberLogarithmicFunctions logarithmicFunctions;
    private final RationalNumberAuxiliaryFunctions auxiliaryFunctions;

    public DefaultController(@Qualifier("rationalNumberGoniometricFunctions") RationalNumberGoniometricFunctions goniometricFunctions,
                             @Qualifier("rationalNumberLogarithmicFunctions") RationalNumberLogarithmicFunctions logarithmicFunctions,
                             @Qualifier("rationalNumberAuxiliaryFunctions") RationalNumberAuxiliaryFunctions auxiliaryFunctions) {
        this.goniometricFunctions = goniometricFunctions;
        this.logarithmicFunctions = logarithmicFunctions;
        this.auxiliaryFunctions = auxiliaryFunctions;
    }

    @GetMapping("{functionName}")
    public String sin(@PathVariable String functionName ) {
        return "examples/" + functionName;
    }

    @PostMapping(MAPPING_SIN)
    @ResponseBody
    public Map<Integer, String> sin(String rationalNumberAsString, int scale, int maximumDegreeOfPolynomial) {
        Map<Integer, String> result = new HashMap<>();

        RationalNumber rationalNumber = RationalNumber.valueOf(rationalNumberAsString);

        Scale.set(scale);
        for (int i = 0; i <= maximumDegreeOfPolynomial; i++) {
            TaylorDegreeOfPolynom.set(i);
            result.put(i, goniometricFunctions.sin(rationalNumber).toString());
        }

        return result;
    }

    @PostMapping(MAPPING_EXP)
    @ResponseBody
    public Map<Integer, String> exp(String rationalNumberAsString, RationalNumberOutputType.PredefinedType outputPredefinedType, int scale, int maximumDegreeOfPolynomial) {
        Map<Integer, String> result = new HashMap<>();

        RationalNumber rationalNumber = RationalNumber.valueOf(rationalNumberAsString);
        RationalNumberOutputType.set(outputPredefinedType);
        Scale.set(scale);
        for (int i = 0; i < maximumDegreeOfPolynomial; i++) {
            TaylorDegreeOfPolynom.set(i);
            result.put(i, logarithmicFunctions.exp(rationalNumber).toString());
        }

        return result;
    }

    @PostMapping(MAPPING_FACULTY)
    @ResponseBody
    public String faculty(@NotBlank(message = "Please specify a not blank number string") String rationalNumberAsString) {
        RationalNumber rationalNumber = RationalNumber.valueOf(rationalNumberAsString);
        RationalNumberOutputType.set(RationalNumberOutputType.PredefinedType.EXACT);

        return auxiliaryFunctions.faculty(rationalNumber).toString();
    }


    @PostMapping(MAPPING_RATIONAL_VALUE_FROM_NUMERATOR_DENOMINATOR)
    @ResponseBody
    public Map<RationalNumberOutputType.PredefinedType, String> rationalValueOfUsingNumeratorDenominator(int numerator, int denominator, int scale) {
        Map<RationalNumberOutputType.PredefinedType, String> result = new HashMap<>();

        RationalNumber rationalNumber = new RationalNumber(numerator, denominator);
        Scale.set(scale);

        RationalNumberOutputType.PredefinedType outputPredefinedType = RationalNumberOutputType.PredefinedType.COMPONENTS;
        result.put(outputPredefinedType, rationalNumber.toString(outputPredefinedType));
        outputPredefinedType = RationalNumberOutputType.PredefinedType.EXACT;
        result.put(outputPredefinedType, rationalNumber.toString(outputPredefinedType));
        outputPredefinedType = RationalNumberOutputType.PredefinedType.TRUNCATED;
        result.put(outputPredefinedType, rationalNumber.toString(outputPredefinedType));

        return result;
    }

    @PostMapping(MAPPING_RATIONAL_VALUE_FROM_STRING)
    @ResponseBody
    public Map<RationalNumberOutputType.PredefinedType, String> rationalValueOfUsingString(@NotBlank(message = "Please specify a not blank number string") String rationalNumberAsString, int scale) {
        Map<RationalNumberOutputType.PredefinedType, String> result = new HashMap<>();

        RationalNumber rationalNumber = RationalNumber.valueOf(rationalNumberAsString);
        Scale.set(scale);

        RationalNumberOutputType.PredefinedType outputPredefinedType = RationalNumberOutputType.PredefinedType.COMPONENTS;
        result.put(outputPredefinedType, rationalNumber.toString(outputPredefinedType));
        outputPredefinedType = RationalNumberOutputType.PredefinedType.EXACT;
        result.put(outputPredefinedType, rationalNumber.toString(outputPredefinedType));
        outputPredefinedType = RationalNumberOutputType.PredefinedType.TRUNCATED;
        result.put(outputPredefinedType, rationalNumber.toString(outputPredefinedType));

        return result;
    }

}

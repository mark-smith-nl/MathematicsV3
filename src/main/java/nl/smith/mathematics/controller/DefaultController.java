package nl.smith.mathematics.controller;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberAuxiliaryFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberGoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberLogarithmicFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

import static nl.smith.mathematics.configuration.constant.NumberConstant.integerValueOf.Scale;
import static nl.smith.mathematics.configuration.constant.NumberConstant.integerValueOf.TaylorDegreeOfPolynom;

@Controller
@RequestMapping("/")
@Validated
public class DefaultController {

    public static final String MAPPING_SIN = "sin";
    public static final String MAPPING_EXP = "exp";
    public static final String MAPPING_FACULTY = "faculty";
    public static final String MAPPING_VALUE_OF_USING_NUMERATOR_DENOMINATOR = "valueOfUsingNumeratorDenominator";
    public static final String MAPPING_VALUE_OF_USING_STRING = "valueOfUsingString";

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

    @GetMapping("echo")
    @ResponseBody
    public String echo(String value) {
        return "Echo: " + value;
    }

    @GetMapping(MAPPING_SIN)
    @ResponseBody
    public Map<Integer, String> sin(String rationalNumberAsString, RationalNumberOutputType.Type outputType, int scale, int maximumDegreeOfPolynomial) {
        Map<Integer, String> result = new HashMap<>();

        RationalNumber rationalNumber = RationalNumber.valueOf(rationalNumberAsString);
        RationalNumberOutputType.set(outputType);
        Scale.set(scale);
        for (int i = 0; i < maximumDegreeOfPolynomial; i++) {
            TaylorDegreeOfPolynom.set(i);
            result.put(i, goniometricFunctions.sin(rationalNumber).toString());
        }

        return result;
    }

    @GetMapping(MAPPING_EXP)
    @ResponseBody
    public Map<Integer, String> exp(String rationalNumberAsString, RationalNumberOutputType.Type outputType, int scale, int maximumDegreeOfPolynomial) {
        Map<Integer, String> result = new HashMap<>();

        RationalNumber rationalNumber = RationalNumber.valueOf(rationalNumberAsString);
        RationalNumberOutputType.set(outputType);
        Scale.set(scale);
        for (int i = 0; i < maximumDegreeOfPolynomial; i++) {
            TaylorDegreeOfPolynom.set(i);
            result.put(i, logarithmicFunctions.exp(rationalNumber).toString());
        }

        return result;
    }

    @GetMapping(MAPPING_FACULTY)
    @ResponseBody
    public Map<Integer, String> faculty(String rationalNumberAsString, RationalNumberOutputType.Type outputType, int scale, int maximumDegreeOfPolynomial) {
        Map<Integer, String> result = new HashMap<>();

        RationalNumber rationalNumber = RationalNumber.valueOf(rationalNumberAsString);
        RationalNumberOutputType.set(outputType);
        Scale.set(scale);
        for (int i = 0; i < maximumDegreeOfPolynomial; i++) {
            TaylorDegreeOfPolynom.set(i);
            result.put(i, logarithmicFunctions.exp(rationalNumber).toString());
        }

        return result;
    }

    @GetMapping(MAPPING_VALUE_OF_USING_NUMERATOR_DENOMINATOR)
    @ResponseBody
    public Map<RationalNumberOutputType.Type, String> valueOf(int numerator, int denominator, int scale) {
        Map<RationalNumberOutputType.Type, String> result = new HashMap<>();

        RationalNumber rationalNumber = new RationalNumber(numerator, denominator);
        Scale.set(scale);

        RationalNumberOutputType.Type outputType = RationalNumberOutputType.Type.COMPONENTS;
        result.put(outputType, rationalNumber.toString(outputType));
        outputType = RationalNumberOutputType.Type.EXACT;
        result.put(outputType, rationalNumber.toString(outputType));
        outputType = RationalNumberOutputType.Type.TRUNCATED;
        result.put(outputType, rationalNumber.toString(outputType));

        return result;
    }

    @GetMapping(MAPPING_VALUE_OF_USING_STRING)
    @ResponseBody
    public Map<RationalNumberOutputType.Type, String> valueOf(@NotBlank(message = "Please specify a not blank number string") String rationalNumberAsString, int scale) {
        Map<RationalNumberOutputType.Type, String> result = new HashMap<>();

        RationalNumber rationalNumber = RationalNumber.valueOf(rationalNumberAsString);
        Scale.set(scale);

        RationalNumberOutputType.Type outputType = RationalNumberOutputType.Type.COMPONENTS;
        result.put(outputType, rationalNumber.toString(outputType));
        outputType = RationalNumberOutputType.Type.EXACT;
        result.put(outputType, rationalNumber.toString(outputType));
        outputType = RationalNumberOutputType.Type.TRUNCATED;
        result.put(outputType, rationalNumber.toString(outputType));

        return result;
    }

}

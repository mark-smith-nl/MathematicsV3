package nl.smith.mathematics.controller;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.development.hva.wef.componentscan.Person;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberAuxiliaryFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberGoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberLogarithmicFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @GetMapping(MAPPING_SIN)
    public String sin() {
        return "examples/sinus.html";
    }

    @PostMapping(MAPPING_SIN)
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
    public String exp() {
        return "examples/exp.html";
    }

    @PostMapping(MAPPING_EXP)
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
    public String faculty() {
        return "examples/faculty.html";
    }

    @PostMapping(MAPPING_FACULTY)
    @ResponseBody
    public String faculty(@NotBlank(message = "Please specify a not blank number string") String rationalNumberAsString) {
        RationalNumber rationalNumber = RationalNumber.valueOf(rationalNumberAsString);
        RationalNumberOutputType.set(RationalNumberOutputType.Type.EXACT);

        return auxiliaryFunctions.faculty(rationalNumber).toString();
    }

    @GetMapping(MAPPING_RATIONAL_VALUE_FROM_NUMERATOR_DENOMINATOR)
    public String rationalValueOfUsingNumeratorDenominator() {
        return "examples/faculty.html";
    }

    @PostMapping(MAPPING_RATIONAL_VALUE_FROM_NUMERATOR_DENOMINATOR)
    @ResponseBody
    public Map<RationalNumberOutputType.Type, String> rationalValueOfUsingNumeratorDenominator(int numerator, int denominator, int scale) {
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

    @GetMapping(MAPPING_RATIONAL_VALUE_FROM_STRING)
    public String rationalValueOfUsingString() {
        return "examples/faculty.html";
    }

    @PostMapping(MAPPING_RATIONAL_VALUE_FROM_STRING)
    @ResponseBody
    public Map<RationalNumberOutputType.Type, String> rationalValueOfUsingString(@NotBlank(message = "Please specify a not blank number string") String rationalNumberAsString, int scale) {
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

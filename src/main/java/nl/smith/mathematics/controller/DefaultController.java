package nl.smith.mathematics.controller;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.configuration.constant.Scale;
import nl.smith.mathematics.configuration.constant.TaylorDegreeOfPolynom;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberGoniometricFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class DefaultController {

    private final RationalNumberGoniometricFunctions goniometricFunctions;


    public DefaultController(@Qualifier("rationalNumberGoniometricFunctions") RationalNumberGoniometricFunctions goniometricFunctions) {
        this.goniometricFunctions = goniometricFunctions;
    }


    @GetMapping("sin")
    public @ResponseBody Map<Integer, String> sin(@RequestParam("rationalNumber") String rationalNumberAsString, RationalNumberOutputType.Type outputType, int scale, int maximumDegreeOfPolynomial) {
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

    @GetMapping("valueOfUsingNumeratorDenominator")
    public @ResponseBody Map<RationalNumberOutputType.Type, String> valueOf(int numerator, int denominator, int scale) {
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

    @GetMapping("valueOfUsingString")
    public @ResponseBody Map<RationalNumberOutputType.Type, String> valueOf(@RequestParam("rationalNumber") String rationalNumberAsString, int scale) {
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

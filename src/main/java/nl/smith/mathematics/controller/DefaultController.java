package nl.smith.mathematics.controller;

import nl.smith.mathematics.mathematicalfunctions.TaylorSeries;
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
    public @ResponseBody Map<Integer, String> sin(@RequestParam("rationalNumber") String rationalNumberAsString, RationalNumber.OutputType outputType, int scale, int maximumDegreeOfPolynomial) {
        Map<Integer, String> result = new HashMap<>();

        RationalNumber rationalNumber = RationalNumber.valueOf(rationalNumberAsString);
        RationalNumber.setOutputType(outputType);
        RationalNumber.setScale(scale);
        for (int i = 0; i < maximumDegreeOfPolynomial; i++) {
            TaylorSeries.setDegreeOfPolynomial(i);
            result.put(i, goniometricFunctions.sin(rationalNumber).toString());
        }

        return result;
    }

    @GetMapping("valueOfUsingNumeratorDenominator")
    public @ResponseBody Map<RationalNumber.OutputType, String> valueOf(int numerator, int denominator, int scale) {
        Map<RationalNumber.OutputType, String> result = new HashMap<>();

        RationalNumber rationalNumber = new RationalNumber(numerator, denominator);
        RationalNumber.setScale(scale);

        RationalNumber.OutputType outputType = RationalNumber.OutputType.COMPONENTS;
        result.put(outputType, rationalNumber.toString(outputType));
        outputType = RationalNumber.OutputType.EXACT;
        result.put(outputType, rationalNumber.toString(outputType));
        outputType = RationalNumber.OutputType.TRUNCATED;
        result.put(outputType, rationalNumber.toString(outputType));

        return result;
    }

    @GetMapping("valueOfUsingString")
    public @ResponseBody Map<RationalNumber.OutputType, String> valueOf(@RequestParam("rationalNumber") String rationalNumberAsString, int scale) {
        Map<RationalNumber.OutputType, String> result = new HashMap<>();

        RationalNumber rationalNumber = RationalNumber.valueOf(rationalNumberAsString);
        RationalNumber.setScale(scale);

        RationalNumber.OutputType outputType = RationalNumber.OutputType.COMPONENTS;
        result.put(outputType, rationalNumber.toString(outputType));
        outputType = RationalNumber.OutputType.EXACT;
        result.put(outputType, rationalNumber.toString(outputType));
        outputType = RationalNumber.OutputType.TRUNCATED;
        result.put(outputType, rationalNumber.toString(outputType));

        return result;
    }
}

package nl.smith.mathematics.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This method <description of functionality>
 *
 * @author m.smithhva.nl
 */
public class FunctionDefinition {

    private String functionName;

    private String rationalNumberArgumentAsString;

    private int scale;

    private  int maximumDegreeOfPolynomial;

    public FunctionDefinition() {
    }

    public FunctionDefinition(String functionName, String rationalNumberArgumentAsString, int scale, int maximumDegreeOfPolynomial) {
        this.functionName = functionName;
        this.rationalNumberArgumentAsString = rationalNumberArgumentAsString;
        this.scale = scale;
        this.maximumDegreeOfPolynomial = maximumDegreeOfPolynomial;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getRationalNumberArgumentAsString() {
        return rationalNumberArgumentAsString;
    }

    public void setRationalNumberArgumentAsString(String rationalNumberArgumentAsString) {
        this.rationalNumberArgumentAsString = rationalNumberArgumentAsString;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getMaximumDegreeOfPolynomial() {
        return maximumDegreeOfPolynomial;
    }

    public void setMaximumDegreeOfPolynomial(int maximumDegreeOfPolynomial) {
        this.maximumDegreeOfPolynomial = maximumDegreeOfPolynomial;
    }
}

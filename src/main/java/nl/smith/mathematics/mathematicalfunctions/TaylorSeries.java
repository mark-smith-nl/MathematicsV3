package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.util.UserSystemContext;

public class TaylorSeries {

    private TaylorSeries() {
        throw new IllegalStateException(String.format("Can not instantiate %s", this.getClass().getCanonicalName()));
    }

    public static final int DEFAULT_DEGREE_OF_POLYNOMIAL = 10;

    private static final String DEGREE_OF_POLYNOMIAL_PROPERTY_NAME = "degreeOfPolynomial";

    public static void setDegreeOfPolynomial(int degreeOfPolynomial) {
        UserSystemContext.setValue(DEGREE_OF_POLYNOMIAL_PROPERTY_NAME, degreeOfPolynomial);
    }

    public static int getDegreeOfPolynomial() {
        return (int) UserSystemContext.getValue(DEGREE_OF_POLYNOMIAL_PROPERTY_NAME).orElse(DEFAULT_DEGREE_OF_POLYNOMIAL);
    }

}

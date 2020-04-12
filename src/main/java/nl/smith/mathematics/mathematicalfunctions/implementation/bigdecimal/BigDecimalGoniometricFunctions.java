package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.mathematicalfunctions.TaylorSeries;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

public class BigDecimalGoniometricFunctions extends GoniometricFunctions<BigDecimal, BigDecimalGoniometricFunctions> {

    private final static String SIBLING_BEAN_NAME = "BIGDECIMALGONIOMETRICFUNCTIONS";

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    public BigDecimalGoniometricFunctions makeSibling() {
        return new BigDecimalGoniometricFunctions();
    }


    protected int degreeOfPolynomial = 5;

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal sin(BigDecimal angle) {
        if (TaylorSeries.getDegreeOfPolynomial() == 0) {
            return BigDecimal.ZERO;
        } else if (TaylorSeries.getDegreeOfPolynomial() == 1) {
            return angle;
        } else {
            BigDecimal two = new BigDecimal(2);
            BigDecimal four = new BigDecimal(4);
            BigDecimal six = new BigDecimal(6);

            BigDecimal T = angle;
            BigDecimal sum = T;

            for (int i = 1; i <= TaylorSeries.getDegreeOfPolynomial(); i++) {
                BigDecimal bdi = new BigDecimal(i);
                T = angle.pow(2).divide(four.multiply(bdi.pow(2)).min(six.multiply(bdi)).add(two)).multiply(T).negate();
                sum = sum.add(T);
                System.out.println(T);
            }
            return sum;
        }
    }

    @Override
    public BigDecimal cos(BigDecimal angle) {
        return new BigDecimal(456);
    }

    @Override
    public BigDecimal getPi() {
        return new BigDecimal("3.14159265358979323846");
    }
}

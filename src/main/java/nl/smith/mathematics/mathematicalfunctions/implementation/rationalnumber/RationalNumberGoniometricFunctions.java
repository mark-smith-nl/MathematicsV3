package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.mathematicalfunctions.TaylorSeries;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.context.annotation.Bean;

public class RationalNumberGoniometricFunctions extends GoniometricFunctions<RationalNumber, RationalNumberGoniometricFunctions> {

    private final static String SIBLING_BEAN_NAME = "RATIONALNUMBERGONIOMETRICFUNCTIONS";

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    @Override
    public RationalNumberGoniometricFunctions makeSibling() {
        return new RationalNumberGoniometricFunctions();
    }


    protected int degreeOfPolynomial = 5;

    /**
     * {@inheritDoc}
     */
    @Override
    public RationalNumber sin(RationalNumber angle) {
        if (TaylorSeries.getDegreeOfPolynomial() == 0) {
            return RationalNumber.ZERO;
        } else if (TaylorSeries.getDegreeOfPolynomial() == 1) {
            return angle;
        } else {
            RationalNumber two = new RationalNumber(2);
            RationalNumber four = new RationalNumber(4);
            RationalNumber six = new RationalNumber(6);

            RationalNumber T = angle;
            RationalNumber sum = T;
            RationalNumber squareAngle = angle.multiply(angle);

            for (int i = 2; i <= TaylorSeries.getDegreeOfPolynomial(); i++) {
                RationalNumber bdi = new RationalNumber(i);
                T = squareAngle.divide(four.multiply(bdi).multiply(bdi).subtract(six.multiply(bdi)).add(two)).multiply(T).negate();
                sum = sum.add(T);
            }
            return sum;
        }
    }

    @Override
    public RationalNumber cos(RationalNumber angle) {
        return new RationalNumber(456);
    }

    @Override
    public RationalNumber getPi() {
        // Number of Metius (https://nl.wikipedia.org/wiki/Pi_(wiskunde))
        return new RationalNumber(355, 113);
    }

}

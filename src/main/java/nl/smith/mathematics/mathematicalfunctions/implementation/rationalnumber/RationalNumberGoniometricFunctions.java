package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.configuration.constant.TaylorDegreeOfPolynom;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.context.annotation.Bean;

import static nl.smith.mathematics.numbertype.RationalNumber.ONE;
import static nl.smith.mathematics.numbertype.RationalNumber.ZERO;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public RationalNumber sin(RationalNumber angle) {
        RationalNumber sum = ZERO;

        Integer iMax = TaylorDegreeOfPolynom.get();
        if (iMax > 0) {
            RationalNumber T = angle;
            sum = sum.add(T);
            RationalNumber squareAngle = angle.multiply(angle);
            for (int i = 3; i <= iMax; i = i + 2) {
                T = T.multiply(squareAngle).divide(i).divide(i - 1).negate();
                sum = sum.add(T);
            }
        }

        return sum;
    }

    @Override
    public RationalNumber cos(RationalNumber angle) {
        RationalNumber sum = ONE;

        Integer iMax = TaylorDegreeOfPolynom.get();
        if (iMax > 0) {
            RationalNumber T = ONE;
            RationalNumber squareAngle = angle.multiply(angle);
            for (int i = 2; i <= iMax; i = i + 2) {
                T = T.multiply(squareAngle).divide(i).divide(i - 1).negate();
                sum = sum.add(T);
            }
        }

        return sum;
    }

}

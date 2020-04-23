package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.configuration.constant.TaylorDegreeOfPolynom;
import nl.smith.mathematics.configuration.constant.bigdecimal.Pi;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal sin(BigDecimal angle) {
        BigDecimal T = angle;
        BigDecimal sum = T;

        Integer iMax = TaylorDegreeOfPolynom.get();
        System.out.println(iMax);
        if (iMax > 0) {
            BigDecimal squareAngle = angle.multiply(angle);
            for (int i = 1; i <= iMax; i++) {
                BigDecimal rationalNumberI = new BigDecimal(i);
                BigDecimal twoTimesI = rationalNumberI.add(rationalNumberI);
                T = T.multiply(squareAngle).divide(twoTimesI.add(ONE)).divide(twoTimesI).negate();
                sum = sum.add(T);
            }
        }
        return sum;
    }

    @Override
    public BigDecimal cos(BigDecimal angle) {
        return sin(Pi.get().divide(new BigDecimal(2)).subtract(angle));
    }

}

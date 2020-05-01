package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.configuration.constant.RoundingMode;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static nl.smith.mathematics.configuration.constant.NumberConstant.integerValueOf;

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
        BigDecimal sum = ZERO;

        int iMax = integerValueOf.TaylorDegreeOfPolynom.get();
        if (iMax > 0) {
            Integer scale = integerValueOf.Scale.get();
            java.math.RoundingMode roundingMode = RoundingMode.get();

            BigDecimal T = angle;
            sum = sum.add(T);
            BigDecimal squareAngle = angle.multiply(angle);
            for (long i = 3; i <= iMax; i = i + 2) {
                T = T.multiply(squareAngle).divide(new BigDecimal(i), scale, roundingMode).divide(new BigDecimal(i - 1), scale, roundingMode).negate();
                sum = sum.add(T);
            }
        }

        return sum;
    }

    @Override
    public BigDecimal cos(BigDecimal angle) {
        BigDecimal sum = ONE;

        int iMax = integerValueOf.TaylorDegreeOfPolynom.get();
        if (iMax > 0) {
            Integer scale = integerValueOf.Scale.get();
            java.math.RoundingMode roundingMode = RoundingMode.get();

            BigDecimal T = ONE;
            BigDecimal squareAngle = angle.multiply(angle);
            for (long i = 2; i <= iMax; i = i + 2) {
                T = T.multiply(squareAngle).divide(new BigDecimal(i), scale, roundingMode).divide(new BigDecimal(i - 1), scale, roundingMode).negate();
                sum = sum.add(T);
            }
        }

        return sum;
    }

    @Override
    public BigDecimal tan(BigDecimal angle) {
        return sin(angle).divide(cos(angle), integerValueOf.Scale.get(), RoundingMode.get());
    }
}

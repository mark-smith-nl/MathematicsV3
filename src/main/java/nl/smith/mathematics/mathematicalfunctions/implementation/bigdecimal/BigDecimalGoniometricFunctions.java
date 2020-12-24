package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RoundingMode;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static nl.smith.mathematics.configuration.constant.NumberConstant.IntegerValueOf.Scale;
import static nl.smith.mathematics.configuration.constant.NumberConstant.IntegerValueOf.TaylorDegreeOfPolynom;

public class BigDecimalGoniometricFunctions extends GoniometricFunctions<BigDecimal, BigDecimalGoniometricFunctions> {

    private static final String SIBLING_BEAN_NAME = "BIG_DECIMAL_GONIOMETRIC_FUNCTIONS";

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

        int iMax = TaylorDegreeOfPolynom.value().get();
        if (iMax > 0) {
            Integer scale = Scale.value().get();
            java.math.RoundingMode roundingMode = RoundingMode.value().get().mathRoundingMode();

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

        int iMax = TaylorDegreeOfPolynom.value().get();
        if (iMax > 0) {
            Integer scale = Scale.value().get();
            java.math.RoundingMode roundingMode = RoundingMode.value().get().mathRoundingMode();

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
        return sin(angle).divide(cos(angle), Scale.value().get(), RoundingMode.value().get().mathRoundingMode());
    }
}

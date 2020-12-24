package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RoundingMode;
import nl.smith.mathematics.configuration.constant.NumberConstant;
import nl.smith.mathematics.mathematicalfunctions.definition.LogarithmicFunctions;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static nl.smith.mathematics.configuration.constant.NumberConstant.IntegerValueOf.Scale;
import static nl.smith.mathematics.configuration.constant.NumberConstant.IntegerValueOf.TaylorDegreeOfPolynom;

public class BigDecimalLogarithmicFunctions extends LogarithmicFunctions<BigDecimal, BigDecimalLogarithmicFunctions> {

    private final static String SIBLING_BEAN_NAME = "BIG_DECIMAL_LOGARITHMIC_FUNCTIONS";

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    @Override
    public BigDecimalLogarithmicFunctions makeSibling() {
        return new BigDecimalLogarithmicFunctions();
    }

    //TODO Tests
    @Override
    public BigDecimal exp(BigDecimal number) {
        BigDecimal T = ONE;
        BigDecimal sum = T;
        for (int i = 1; i <= TaylorDegreeOfPolynom.value().get(); i++) {
            T = T.multiply(number).divide(new BigDecimal(i), Scale.value().get(), RoundingMode.value().get().mathRoundingMode());
            sum = sum.add(T);
        }

        return sum;
    }

    //TODO Tests
    @Override
    public BigDecimal ln(BigDecimal number) {
        BigDecimal sum = ZERO;

        BigDecimal euler = NumberConstant.BigDecimalValueOf.Euler.value().get();
        while (number.compareTo(ONE) > 0) {
            sum = sum.add(ONE);
            number = number.divide(euler, Scale.value().get(), RoundingMode.value().get().mathRoundingMode());
        }

        BigDecimal delta = ONE.subtract(number);


        int iMax = TaylorDegreeOfPolynom.value().get();
        if (!delta.equals(ZERO) && iMax > 0) {
            sum = sum.subtract(delta);
            BigDecimal deltaRaiseToPowI = delta;
            for (int i = 2; i <= iMax; i++) {
                deltaRaiseToPowI = deltaRaiseToPowI.multiply(delta);
                sum = sum.subtract(deltaRaiseToPowI.divide(new BigDecimal(i)));
            }
        }

        return sum;
    }

    //TODO Tests
    @Override
    public BigDecimal power(BigDecimal number, BigDecimal power) {
        if (power.signum() == -1) {
            return ONE.divide(power(number, power.abs()));
        } else if (power.signum() == 0) {
            return ONE;
        }

        BigDecimal result = ONE;
        while (power.compareTo(ONE) > -1) {
            result = result.multiply(number);
            power = power.subtract(ONE);
        }

        return power.equals(ZERO) ? result : result.multiply(exp(power.multiply(ln(number))));
    }

    //TODO Tests
    @Override
    public BigDecimal sqrt(BigDecimal number) {
        return power(number, new BigDecimal("0.5"));
    }
}

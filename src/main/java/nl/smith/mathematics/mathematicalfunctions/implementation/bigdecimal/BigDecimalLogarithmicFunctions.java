package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.configuration.constant.RoundingMode;
import nl.smith.mathematics.configuration.constant.Scale;
import nl.smith.mathematics.configuration.constant.TaylorDegreeOfPolynom;
import nl.smith.mathematics.configuration.constant.bigdecimal.Euler;
import nl.smith.mathematics.mathematicalfunctions.definition.LogarithmicFunctions;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;


public class BigDecimalLogarithmicFunctions extends LogarithmicFunctions<BigDecimal, BigDecimalLogarithmicFunctions> {

    private final static String SIBLING_BEAN_NAME = "BIGDECIMALLOGARITHMICFUNCTIONS";

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    @Override
    public BigDecimalLogarithmicFunctions makeSibling() {
        return new BigDecimalLogarithmicFunctions();
    }

    @Override
    public BigDecimal exp(BigDecimal number) {
        BigDecimal T = ONE;
        BigDecimal sum = T;
        for (int i = 1; i <= TaylorDegreeOfPolynom.get(); i++) {
            T = T.multiply(number).divide(new BigDecimal(i), Scale.get(), RoundingMode.get());
            sum = sum.add(T);
        }

        return sum;
    }

    @Override
    public BigDecimal ln(BigDecimal number) {
        BigDecimal sum = ZERO;

        BigDecimal euler = Euler.get();
        while (number.compareTo(ONE) > 0) {
            sum = sum.add(ONE);
            number = number.divide(euler);
        }

        BigDecimal delta = ONE.subtract(number);
        int iMax = TaylorDegreeOfPolynom.get();
        if (iMax > 0) {
            sum = sum.subtract(delta);
            BigDecimal deltaRaiseToPowI = delta;
            for (int i = 2; i <= iMax; i++) {
                deltaRaiseToPowI = deltaRaiseToPowI.multiply(delta);
                sum = sum.subtract(deltaRaiseToPowI.divide(new BigDecimal(i)));
            }
        }

        return sum;
    }

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

    @Override
    public BigDecimal sqrt(BigDecimal number) {
        return power(number, new BigDecimal(0.5));
    }
}
package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.configuration.constant.TaylorDegreeOfPolynom;
import nl.smith.mathematics.configuration.constant.rationalnumber.Euler;
import nl.smith.mathematics.mathematicalfunctions.definition.LogarithmicFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.context.annotation.Bean;

import static nl.smith.mathematics.numbertype.RationalNumber.ONE;
import static nl.smith.mathematics.numbertype.RationalNumber.ZERO;

public class RationalNumberLogarithmicFunctions extends LogarithmicFunctions<RationalNumber, RationalNumberLogarithmicFunctions> {

    private final static String SIBLING_BEAN_NAME = "RATIONALNUMBERLOGARITHMICFUNCTIONS";

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    @Override
    public RationalNumberLogarithmicFunctions makeSibling() {
        return new RationalNumberLogarithmicFunctions();
    }

    @Override
    public RationalNumber exp(RationalNumber number) {
        if (number.signum() < 0 ) {
            return ONE.divide(exp(number.abs()));
        }

        if (number.compareTo(ONE) > 0) {
            RationalNumber euler = Euler.get();
            RationalNumber[] divideAndRemainder = number.divideAndRemainder(ONE);
            RationalNumber i = divideAndRemainder[0];
            RationalNumber result = euler;
            do {
                result = result.multiply(euler);
                i = i.subtract(ONE);
            } while (!i.equals(ONE));

            return result.multiply(exp(divideAndRemainder[1]));
        }

        if (number.equals(ZERO)) {
           return ONE;
        }

        RationalNumber T = ONE;
        RationalNumber sum = T;
        for (int i = 1; i <= TaylorDegreeOfPolynom.get(); i++) {
            T = T.multiply(number).divide(i);
            sum = sum.add(T);
        }

        return sum;
    }



    @Override
    public RationalNumber ln(RationalNumber number) {
        RationalNumber sum = ZERO;

        RationalNumber euler = Euler.get();
        while (number.compareTo(ONE) > 0) {
            sum = sum.add(ONE);
            number = number.divide(euler);
        }

        RationalNumber delta = ONE.subtract(number);


        int iMax = TaylorDegreeOfPolynom.get();
        if (!delta.equals(ZERO) && iMax > 0) {
            sum = sum.subtract(delta);
            RationalNumber deltaRaiseToPowI = delta;
            for (int i = 2; i <= iMax; i++) {
                deltaRaiseToPowI = deltaRaiseToPowI.multiply(delta);
                sum = sum.subtract(deltaRaiseToPowI.divide(new RationalNumber(i)));
            }
        }

        return sum;
    }

    @Override
    public RationalNumber power(RationalNumber number, RationalNumber power) {
        if (power.signum() == -1) {
            return ONE.divide(power(number, power.abs()));
        } else if (power.signum() == 0) {
            return ONE;
        }

        RationalNumber result = ONE;
        while (power.compareTo(ONE) > -1) {
            result = result.multiply(number);
            power = power.subtract(ONE);
        }

        return power.equals(ZERO) ? result : result.multiply(exp(power.multiply(ln(number))));
    }

    @Override
    public RationalNumber sqrt(RationalNumber number) {
        return power(number, new RationalNumber(1, 2));
    }

}

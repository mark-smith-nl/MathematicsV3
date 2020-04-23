package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.configuration.constant.TaylorDegreeOfPolynom;
import nl.smith.mathematics.mathematicalfunctions.definition.LogarithmicFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.context.annotation.Bean;

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
        RationalNumber T = RationalNumber.ONE;
        RationalNumber sum = T;
        for (int i = 1; i <= TaylorDegreeOfPolynom.get(); i++) {
            T = T.multiply(number).divide(i);
            sum = sum.add(T);
        }

        return sum;
    }
}

package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.context.annotation.Bean;

import javax.validation.constraints.NotNull;

public class RationalNumberArithmeticFunctions extends ArithmeticFunctions<RationalNumber, RationalNumberArithmeticFunctions> {

    private final static String SIBLING_BEAN_NAME = "RATIONAL_NUMBER_ARITHMETIC_FUNCTIONS";

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    @Override
    public RationalNumberArithmeticFunctions makeSibling() {
        return new RationalNumberArithmeticFunctions();
    }

    @Override
    public RationalNumber minus(@NotNull RationalNumber number) {
        return number.negate();
    }

    @Override
    public RationalNumber plus(@NotNull RationalNumber number, @NotNull RationalNumber augend) {
        return number.add(augend);
    }

    @Override
    public RationalNumber minus(@NotNull RationalNumber number, @NotNull RationalNumber subtrahend) {
        return number.subtract(subtrahend);
    }

    @Override
    public RationalNumber multiplyBy(@NotNull RationalNumber number, @NotNull RationalNumber multiplicand) {
        return number.multiply(multiplicand);
    }

    @Override
    public RationalNumber divideBy(@NotNull RationalNumber number, @NotNull RationalNumber divisor) {
        return number.divide(divisor);
    }
}

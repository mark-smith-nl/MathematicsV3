package nl.smith.mathematics.mathematicalfunctions.implementation;

import nl.smith.mathematics.mathematicalfunctions.definition.AbstractExampleRecursiveFunctionContainer;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

public class BigDecimalExampleRecursiveFunctionContainer extends AbstractExampleRecursiveFunctionContainer<BigDecimal, BigDecimalExampleRecursiveFunctionContainer> {

    private final static String SIBLING_BEAN_NAME = "BIG_DECIMAL_TEST_FUNCTION_CONTAINER";

    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    public BigDecimalExampleRecursiveFunctionContainer makeSibling() {
        return new BigDecimalExampleRecursiveFunctionContainer();
    }

    @Override
    public BigDecimal four(BigDecimal number) {
        return number;
    }

    public BigDecimal five(BigDecimal number) {
        return number;
    }

    @Override
    public BigDecimal six(BigDecimal ... number) {
        return number[0];
    }

    public BigDecimal seven(BigDecimal number) {
        return number;
    }
}

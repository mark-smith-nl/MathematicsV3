package nl.smith.mathematics.mathematicalfunctions.implementation;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.definition.AbstractTestFunctionContainer;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

public class BigDecimalTestFunctionContainer extends AbstractTestFunctionContainer<BigDecimal, BigDecimalTestFunctionContainer> {

    private final static String SIBLING_BEAN_NAME = "BIGDECIMALTESTFUNCTIONCONTAINER";

    @Override
    public BigDecimal four(BigDecimal numberrrrr) {
        return numberrrrr;
    }

    @MathematicalFunction(description = "functionOne")
    public BigDecimal five(BigDecimal number) {
        return number;
    }

    @Override
    public BigDecimal six(BigDecimal number) {
        return number;
    }

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    public BigDecimalTestFunctionContainer makeSibling() {
        return new BigDecimalTestFunctionContainer();
    }

}

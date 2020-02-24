package nl.smith.mathematics.mathematicalfunctions.implementation;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.definition.AbstractTestFunctionContainer;

import java.math.BigDecimal;

public class BigDecimalTestFunctionContainer extends AbstractTestFunctionContainer<BigDecimal> {

    @Override
    public BigDecimal four(BigDecimal number) {
        return number;
    }

    @MathematicalFunction(description = "functionOne")
    public BigDecimal five(BigDecimal number) {
        return number;
    }

    @Override
    public BigDecimal six(BigDecimal number) {
        return number;
    }
}

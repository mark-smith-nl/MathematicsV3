package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

import java.math.BigDecimal;

public abstract class AbstractTestFunctionContainer<T extends Number> extends FunctionContainer<T> {

    @Override
    public String getDescription() {
        return "AbstractFunctionContainer";
    }

    @MathematicalFunction(description = "functionOne")
    public T one(T number) {
        return number;
    }

    @MathematicalFunction(description = "functionTwo")
    private T two(T number) {
        return number;
    }

    public T three(T number) {
        return number;
    }

    @MathematicalFunction(description = "functionFour")
    public abstract T four(T number);

    public abstract BigDecimal six(BigDecimal number);
}

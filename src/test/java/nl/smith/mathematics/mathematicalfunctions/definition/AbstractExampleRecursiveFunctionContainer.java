package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;

import java.math.BigDecimal;

@MathematicalFunctionContainer(name = "Name AbstractExampleRecursiveFunctionContainer", description = "Description AbstractExampleRecursiveFunctionContainer")
public abstract class AbstractExampleRecursiveFunctionContainer<N extends Number, S extends AbstractExampleRecursiveFunctionContainer> extends RecursiveFunctionContainer<N, S> {

    public AbstractExampleRecursiveFunctionContainer() {
        super(AbstractExampleRecursiveFunctionContainer.class);
    }

    // Note: Annotation will be ignored since the method is not abstract.
    @MathematicalFunction(description = "function: One")
    public N one(N number) {
        return number;
    }

    // Note: Annotation will be ignored since the method is not abstract.
    @MathematicalFunction(description = "function: Two")
    private N two(N number) {
        return number;
    }

    // No annotation. Function will be ignored.
    public N three(N number) {
        return number;
    }

    public abstract BigDecimal four(BigDecimal number);

    @MathematicalFunction(description = "function: Five")
    public abstract N five(N number);

    @MathematicalFunction(name = "Method 6", description = "function: Six")
    public abstract N six(N ... number);

   // @MathematicalFunction(name = "Method 6", description = "function: Six")
  //  public abstract N seven(N ... number);


}

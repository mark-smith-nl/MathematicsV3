package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

public abstract class StatisticalFunctions<T extends Number> extends FunctionContainer<T> {

    @Override
    public String getDescription() {
        return "Statistical methods: mean, standard deviation, maimum, minimum";
    }

    @MathematicalFunction(description = "Sum of a set of numbers")
    public abstract T sum(T ... numbers);

    @MathematicalFunction(description = "Product of a set of numbers")
    public abstract T prod(T ... numbers);

    @MathematicalFunction(description = "Standard deviation of a set of numbers")
    public abstract T deviation(T ... numbers);

    @MathematicalFunction(description = "The mean value of a set of numbers")
    public abstract T mean(T ... numbers);

}
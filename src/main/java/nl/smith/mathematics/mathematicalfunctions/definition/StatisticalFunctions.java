package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

import javax.validation.constraints.NotNull;

public abstract class StatisticalFunctions<T extends Number, S extends StatisticalFunctions> extends FunctionContainer<T, S> {

    @Override
    public String getDescription() {
        return "Statistical methods: mean, standard deviation, maimum, minimum";
    }

    @MathematicalFunction(description = "Sum of a set of numbers")
    //TODO Add constraint
    //TODO Test omplementations
    public abstract T sum(T ... numbers);

    @MathematicalFunction(description = "Product of a set of numbers")
    //TODO Add constraint
    //TODO Test omplementations
    public abstract T prod(@NotNull T ... numbers);

    @MathematicalFunction(description = "Average of a set of numbers")
    //TODO Add constraint
    //TODO Test omplementations
    public abstract T average(T ... numbers);

    @MathematicalFunction(description = "Standard deviation of a set of numbers")
    //TODO Add constraint
    //TODO Test omplementations
    public abstract T deviation(T ... numbers);

}
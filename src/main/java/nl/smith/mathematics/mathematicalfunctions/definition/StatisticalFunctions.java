package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;

import javax.validation.constraints.NotNull;

@MathematicalFunctionContainer(name = "Statistical methods", description = "Statistical methods: mean, standard deviation, maximum, minimum")
public abstract class StatisticalFunctions<N extends Number, S extends StatisticalFunctions<N, S>> extends RecursiveFunctionContainer<N, S> {

    public StatisticalFunctions() {
        super(StatisticalFunctions.class);
    }

    @MathematicalFunction(description = "Sum of a set of numbers")
    //TODO Add constraint
    //TODO Test implementations
    public abstract N sum(N ... numbers);

    @MathematicalFunction(description = "Product of a set of numbers")
    //TODO Add constraint
    //TODO Test implementations
    public abstract N prod(@NotNull N ... numbers);

    @MathematicalFunction(description = "Average of a set of numbers")
    //TODO Add constraint
    //TODO Test implementations
    public abstract N average(N ... numbers);

    @MathematicalFunction(description = "Standard deviation of a set of numbers")
    //TODO Add constraint
    //TODO Test implementations
    public abstract N deviation(N ... numbers);

    @MathematicalFunction(description = "Standard deviation of a set of numbers")
    //TODO Add constraint
    //TODO Test implementations
    public abstract N keyNumber(N number, N ... numbers);

    @MathematicalFunction(description = "SQUARE")
    //TODO Add constraint
    //TODO Test implementations
    public abstract N[] square(N ... numbers);

}

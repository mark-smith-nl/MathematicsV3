package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;

import javax.validation.constraints.NotNull;

@MathematicalFunctionContainer(name = "Statistical methods", description = "Statistical methods: mean, standard deviation, maimum, minimum")
public abstract class StatisticalFunctions<N extends Number, S extends StatisticalFunctions> extends RecursiveFunctionContainer<N, S> {

    public StatisticalFunctions() {
        super(StatisticalFunctions.class);
    }

    @MathematicalFunction(description = "Sum of a set of numbers")
    //TODO Add constraint
    //TODO Test omplementations
    public abstract N sum(N ... numbers);

    @MathematicalFunction(description = "Product of a set of numbers")
    //TODO Add constraint
    //TODO Test omplementations
    public abstract N prod(@NotNull N ... numbers);

    @MathematicalFunction(description = "Average of a set of numbers")
    //TODO Add constraint
    //TODO Test omplementations
    public abstract N average(N ... numbers);

    @MathematicalFunction(description = "Standard deviation of a set of numbers")
    //TODO Add constraint
    //TODO Test omplementations
    public abstract N deviation(N ... numbers);

}
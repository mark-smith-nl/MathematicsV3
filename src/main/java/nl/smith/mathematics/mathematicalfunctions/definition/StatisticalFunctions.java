package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctions;
import nl.smith.mathematics.mathematicalfunctions.AbstractFunctionContainer;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@MathematicalFunctions(name = "Statistical functions", description = "Statistical methods: mean, standard deviatioin, maimum, minimum")
public abstract class StatisticalFunctions<T extends Number> extends AbstractFunctionContainer<T> {

    @MathematicalFunction(description = "Standard deviation of a set of numbers")
    public abstract T deviation(@Min(value = 1, message = "From must be greater than 0") T numbers);

    @MathematicalFunction(description = "The mean value of a set of numbers")
    public abstract T mean(@Min(value = 1, message = "From must be greater than 0") T numbers);

}
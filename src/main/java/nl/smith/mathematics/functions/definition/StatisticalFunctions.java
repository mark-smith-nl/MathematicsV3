package nl.smith.mathematics.functions.definition;

import javax.validation.constraints.Min;

import org.springframework.validation.annotation.Validated;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;

@Validated
@MathematicalFunctionContainer(description = "Statistical methods: mean, standard deviatioin, maimum, minimum")
public abstract class StatisticalFunctions<T extends Number> extends FunctionContainer<T> {

	@MathematicalFunction(description = "Standard deviation of a set of numbers")
	public abstract T deviation(@Min(value = 1, message = "From must be greater than 0") T numbers);

}
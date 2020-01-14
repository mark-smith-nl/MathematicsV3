package nl.smith.mathematics.functions.definition;

import javax.validation.constraints.NotEmpty;

import nl.smith.mathematics.annotation.MathematicalFunction;

public abstract class StatisticalFunctions<T extends Number> {

	// @SuppressWarnings("unchecked")
	// T min(T... numbers);

	// @SuppressWarnings("unchecked")
	// T max(T... numbers);

	// @SuppressWarnings("unchecked")
	// T average(@NotEmpty T... numbers);

	@MathematicalFunction(description = "Standard deviation of a set of numbers1234")
	@SuppressWarnings("unchecked")
	public abstract T deviation(@NotEmpty T numbers);

}
package nl.smith.mathematics.functions.definition;

import javax.validation.constraints.NotEmpty;

import nl.smith.mathematics.annotation.MathematicalFunction;

public interface StatisticalFunctions<T extends Number> extends Functions<T> {

	// @SuppressWarnings("unchecked")
	// T min(T... numbers);

	// @SuppressWarnings("unchecked")
	// T max(T... numbers);

	// @SuppressWarnings("unchecked")
	// T average(@NotEmpty T... numbers);

	@MathematicalFunction(description = "Standard deviation of a set of numbers")
	@SuppressWarnings("unchecked")
	T deviation(@NotEmpty T... numbers);

}
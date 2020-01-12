package nl.smith.mathematics.functions.definition;

import javax.validation.constraints.NotEmpty;

public interface StatisticalFunctions<T extends Number> extends Functions<T> {

	@SuppressWarnings("unchecked")
	T min(T... numbers);

	@SuppressWarnings("unchecked")
	T max(T... numbers);

	@SuppressWarnings("unchecked")
	T average(@NotEmpty T... numbers);

	@SuppressWarnings("unchecked")
	T deviation(T... numbers);

}
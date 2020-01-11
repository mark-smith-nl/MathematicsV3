package nl.smith.mathematics.functions.definition;

import javax.validation.constraints.NotEmpty;

public abstract class StatisticalFunctions<T extends Number> extends Functions<T> {

	@SuppressWarnings("unchecked")
	public abstract T min(T... numbers);

	@SuppressWarnings("unchecked")
	public abstract T max(T... numbers);

	@SuppressWarnings("unchecked")
	public abstract T average(@NotEmpty T... numbers);

	@SuppressWarnings("unchecked")
	public abstract T deviation(T... numbers);

}
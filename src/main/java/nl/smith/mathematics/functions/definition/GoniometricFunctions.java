package nl.smith.mathematics.functions.definition;

import javax.validation.constraints.NotEmpty;

public abstract class GoniometricFunctions<T extends Number> extends Functions<T> {

	public abstract T sin(@NotEmpty T angle);

	public abstract T cos(@NotEmpty T angle);

	public abstract T tan(@NotEmpty T angle);

}
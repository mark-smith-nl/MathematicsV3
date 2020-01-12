package nl.smith.mathematics.functions.definition;

import javax.validation.constraints.NotEmpty;

public interface GoniometricFunctions<T extends Number> extends Functions<T> {

	T sin(@NotEmpty T angle);

	T cos(@NotEmpty T angle);

	T tan(@NotEmpty T angle);

}
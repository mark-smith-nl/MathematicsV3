package nl.smith.mathematics.functions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;

@MathematicalFunctionContainer(description = "Goniometric methods (sin, cos, tan, arcsin, arccos, arctan")
public abstract class GoniometricFunctions<T extends Number> extends FunctionContainer<T> {

	@MathematicalFunction(description = "The sinus of an angle")
	public abstract T sin(T angle);

}
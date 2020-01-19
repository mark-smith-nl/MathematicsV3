package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctions;

@MathematicalFunctions (description = "Goniometric methods (sin, cos, tan, arcsin, arccos, arctan")
public abstract class GoniometricFunctions<T extends Number> extends FunctionContainer<T> {

	@MathematicalFunction(description = "The sinus of an angle")
	public abstract T sin(T angle);

}
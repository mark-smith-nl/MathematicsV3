package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctions;
import nl.smith.mathematics.mathematicalfunctions.AbstractFunctionContainer;

@MathematicalFunctions (name = "Goniometric functions", description = "Goniometric methods (sin, cos, tan, arcsin, arccos, arctan")
public abstract class GoniometricFunctions<T extends Number> extends AbstractFunctionContainer<T> {

  @MathematicalFunction (description = "The sinus of an angle")
  public abstract T sin(T angle);

  @MathematicalFunction (description = "The cosinus of an angle")
  public abstract T cos(T angle);

}
package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

public abstract class GoniometricFunctions<T extends Number> extends FunctionContainer<T> {

  @Override
  public String getDescription() {
    return "Goniometric methods (sin, cos, tan, arcsin, arccos, arctan";
  }

  @MathematicalFunction (description = "The sinus of an angle")
  public abstract T sin(T angle);

  @MathematicalFunction (description = "The cosinus of an angle")
  public abstract T cos(T angle);

}
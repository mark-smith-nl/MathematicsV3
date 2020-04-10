package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

public abstract class GoniometricFunctions<T extends Number, S extends GoniometricFunctions> extends FunctionContainer<T, S> {

  @Override
  public String getDescription() {
    return "Goniometric methods (sin, cos, tan, arcsin, arccos, arctan";
  }

  /**
   *              ∞
   *    sin(x) = -∑ (-1)ⁱx²ⁱ⁻¹/(2i - 1)!
   *             i=1
   *
   *                  ∞
   *    sin(x) = T₁ - ∑ Tᵢ₋₁ x²/(4i²-6i+2) T₁ = x
   *                 i=2
   *
   */
  @MathematicalFunction (description = "The sinus of an angle")
  public abstract T sin(T angle);

  @MathematicalFunction (description = "The cosinus of an angle")
  public abstract T cos(T angle);

}
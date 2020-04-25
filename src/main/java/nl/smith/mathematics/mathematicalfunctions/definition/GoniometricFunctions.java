package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

import javax.validation.constraints.NotNull;

public abstract class GoniometricFunctions<T extends Number, S extends GoniometricFunctions> extends FunctionContainer<T, S> {

  @Override
  public String getDescription() {
    return "Goniometric methods (sin, cos, tan, arcsin, arccos, arctan";
  }

  /**
   *              ∞
   *    sin(x) = -∑ (-1)ⁱx²ⁱ⁺¹/(2i + 1)!
   *             i=0
   *                  ∞
   *    sin(x) = T₀ - ∑ Tᵢ₋₁ x²/(4i² + 2i) T₁ = x
   *                 i=1
   *
   */
  @MathematicalFunction (description = "The sinus of an angle")
  //TODO Test omplementations
  //TODO Implement Modulo
  public abstract T sin(@NotNull T angle);

  @MathematicalFunction (description = "The cosinus of an angle")
  //TODO Test omplementations
  //TODO Implement Modulo
  //TODO Change implementation cos(x) = f(sin(x))
  public abstract T cos(@NotNull T angle);


  @MathematicalFunction (description = "The tangent of an angle")
  //TODO Test omplementations
  public abstract T tan(@NotNull T angle);


}
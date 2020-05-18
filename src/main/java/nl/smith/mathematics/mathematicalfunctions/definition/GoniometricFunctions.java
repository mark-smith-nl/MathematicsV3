package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;

import javax.validation.constraints.NotNull;

@MathematicalFunctionContainer(name = "Goniometric methods", description = "Goniometric methods (sin, cos, tan, arcsin, arccos, arctan")
public abstract class GoniometricFunctions<N extends Number, S extends GoniometricFunctions> extends RecursiveFunctionContainer<N, S> {

  public GoniometricFunctions() {
    super(GoniometricFunctions.class);
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
  public abstract N sin(@NotNull N angle);

  @MathematicalFunction (description = "The cosinus of an angle")
  //TODO Test omplementations
  //TODO Implement Modulo
  //TODO Change implementation cos(x) = f(sin(x))
  public abstract N cos(@NotNull N angle);


  @MathematicalFunction (description = "The tangent of an angle")
  //TODO Test omplementations
  public abstract N tan(@NotNull N angle);


}
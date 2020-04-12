package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

public abstract class LogarithmicFunctions<T extends Number, S extends LogarithmicFunctions> extends FunctionContainer<T, S> {

  @Override
  public String getDescription() {
    return "Logarithmic methods: exp, ln";
  }


  /**
   *             ∞
   *    exp(x) = ∑ xⁱ/i!
   *            i=1
   *                  ∞
   *    exp(x) = T₀ + ∑ Tᵢ x/i T₀ = 1
   *                 i=1
   *
   */
  @MathematicalFunction(description = "The exp of a number")
  public abstract T exp(T number);
}
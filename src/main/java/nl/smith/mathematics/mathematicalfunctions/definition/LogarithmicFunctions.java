package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

import javax.validation.constraints.NotNull;

public abstract class LogarithmicFunctions<T extends Number, S extends LogarithmicFunctions> extends FunctionContainer<T, S> {

  @Override
  public String getDescription() {
    return "Logarithmic methods: exp, ln";
  }


  /**
   *             ∞
   *    exp(x) = ∑ xⁱ/i! = 1 + x/1! + x²/2! + x³/3! + x⁴/4! + ...
   *            i=0
   *                  ∞
   *    exp(x) = T₀ + ∑ Tᵢ₋₁ x/i T₀ = 1
   *                 i=1
   *
   */
  @MathematicalFunction(description = "The exp of a number")
  //TODO test implementations
  public abstract T exp(@NotNull T number);

  /**
   *
   *                  ∞
   *     ln(1 + x) = -∑ (-1)ⁱxⁱ/i = 0 - x + x²/2 + x³/3 - x⁴/4 + ...
   *                 i=1
   *
   *                        ∞
   *     ln(1 + x) = 0 + x -∑ x Tᵢ₋₁ (i - 1)/i T₁ = x
   *                       i=2
   *
   * @param number
   * @return
   */
  @MathematicalFunction(description = "The natural logarithm of a number")
  //TODO test implementations and constraint
  public abstract T ln(@NotNull @IsLargerThan("0") T number);

  /** Note: Zero to the power of zero, denoted by 0⁰ = 1, is a mathematical expression with no agreed-upon value.
   * The most common possibilities are 1 or leaving the expression undefined, with justifications existing for each, depending on context.
   * In algebra and combinatorics, the generally agreed upon value is 00 = 1, whereas in mathematical analysis, the expression is sometimes left undefined.
   * Computer programs also have differing ways of handling this expression.
   * In this program the value 1 is returned.
   */
  @MathematicalFunction(description = "The number raised to the power")
  //TODO test implementations and constraint
  public abstract T power(@NotNull T number, @NotNull T power);

  @MathematicalFunction(description = "The square root of a number")
  //TODO test implementations and constraint
  // Other implementation
  public abstract T sqrt(@NotNull T number);

}
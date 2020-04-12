package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;
import nl.smith.mathematics.util.UserSystemContext;

public abstract class GoniometricFunctions<T extends Number, S extends GoniometricFunctions> extends FunctionContainer<T, S> {

  @Override
  public String getDescription() {
    return "Goniometric methods (sin, cos, tan, arcsin, arccos, arctan";
  }

  public static final AngleType DEFAULT_ANGLE_TYPE = AngleType.RAD;

  public static void setAngleType(AngleType angleType) {
    UserSystemContext.setValue(AngleType.class.getCanonicalName(), angleType);;
  }

  public static AngleType getAngleType() {
    return UserSystemContext.getSingleValueOfType(AngleType.class).orElse(DEFAULT_ANGLE_TYPE);
  }

  public enum AngleType {
    DEG,
    GRAD,
    RAD
  }

  /**
   *              ∞
   *    sin(x) = -∑ (-1)ⁱx²ⁱ⁻¹/(2i - 1)!
   *             i=2
   *                       ∞
   *    sin(x) = T₀ + T₁ - ∑ Tᵢ₋₁ x²/(4i²-6i+2) T₀ = 0, T₁ = x
   *                      i=2
   *
   */
  @MathematicalFunction (description = "The sinus of an angle")
  public abstract T sin(T angle);

  @MathematicalFunction (description = "The cosinus of an angle")
  public abstract T cos(T angle);

 // public abstract T angleAsRadians(T angle);

  public abstract T getPi();
}
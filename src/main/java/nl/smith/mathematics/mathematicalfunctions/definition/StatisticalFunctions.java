package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctions;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@Validated @MathematicalFunctions (name = "Statistical functions", description = "Statistical methods: mean, standard deviatioin, maimum, minimum") public abstract class StatisticalFunctions<T extends Number>
  extends AbstractFunctionContainer<T> {

  @MathematicalFunction (description = "Standard deviation of a set of numbers")
  public abstract T deviation(@Min (value = 1, message = "From must be greater than 0") T numbers);

}
package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.ArithmeticOperator;
import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsBetween;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;

import javax.validation.constraints.NotNull;

@MathematicalFunctionContainer(name = "Arithmetic functions", description = "Arithmetic methods: add, subtract, multipy and divide")
public abstract class ArithmeticFunctions<N extends Number, S extends ArithmeticFunctions<?, ?>> extends RecursiveFunctionContainer<N, S> {

    public ArithmeticFunctions() {
        super(ArithmeticFunctions.class);
    }

    @ArithmeticOperator('+')
    @MathematicalFunction(description = "Sum of two numbers")
    public abstract N sum(@NotNull N number, @NotNull N augend);

    @ArithmeticOperator('+')
    @MathematicalFunction(description = "Subtraction of two numbers")
    public abstract N subtract(@NotNull N number, @NotNull N subtrahend);

    @ArithmeticOperator('*')
    @MathematicalFunction(description = "Multiplication of two numbers")
    public abstract N multiply(@NotNull N number, @NotNull N multiplicand);

    @ArithmeticOperator('/')
    @MathematicalFunction(description = "Division of two numbers")
    public abstract N divide(@NotNull N number, @NotNull N divisor);
}

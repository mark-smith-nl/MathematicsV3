package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;

import javax.validation.constraints.NotNull;

@MathematicalFunctionContainer(name = "Arithmetic functions", description = "Arithmetic methods: add, subtract, multiply and divide")
public abstract class ArithmeticFunctions<N extends Number, S extends ArithmeticFunctions<?, ?>> extends RecursiveFunctionContainer<N, S> {

    public ArithmeticFunctions() {
        super(ArithmeticFunctions.class);
    }

    @MathematicalFunction(name = "-", description = "Negate number")
    public abstract N minus(@NotNull N number);

    @MathematicalFunction(name = "+", description = "Sum of two numbers")
    public abstract N plus(@NotNull N number, @NotNull N augend);

    @MathematicalFunction(name = "-", description = "Subtraction of two numbers")
    public abstract N minus(@NotNull N number, @NotNull N subtrahend);

    @MathematicalFunction(name = "*", description = "Multiplication of two numbers")
    public abstract N multiplyBy(@NotNull N number, @NotNull N multiplicand);

    @MathematicalFunction(name = "/", description = "Division of two numbers")
    public abstract N divideBy(@NotNull N number, @NotNull N divisor);
}

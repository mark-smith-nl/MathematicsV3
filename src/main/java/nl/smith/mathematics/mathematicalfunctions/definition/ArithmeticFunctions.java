package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.ArithmeticMathematicalFunction;
import nl.smith.mathematics.annotation.ArithmeticMathematicalFunction.Priority;
import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;

import javax.validation.constraints.NotNull;

import static nl.smith.mathematics.annotation.ArithmeticMathematicalFunction.*;
import static nl.smith.mathematics.annotation.MathematicalFunction.*;

@MathematicalFunctionContainer(name = "Arithmetic functions", description = "Arithmetic methods: add, subtract, multiply and divide")
public abstract class ArithmeticFunctions<N extends Number, S extends ArithmeticFunctions<N, ?>> extends RecursiveFunctionContainer<N, S> {

    public ArithmeticFunctions() {
        super(ArithmeticFunctions.class);
    }

    @ArithmeticMathematicalFunction(symbol = '-', description = "Negate number", type = Type.UNARY, priority = Priority.LOW)
    public abstract N minus(@NotNull N number);

    @ArithmeticMathematicalFunction(symbol = '+', description = "Sum of two numbers", type = Type.BINARY,  priority = Priority.LOW)
    public abstract N plus(@NotNull N number, @NotNull N augend);

    @ArithmeticMathematicalFunction(symbol = '-', description = "Subtraction of two numbers", type = Type.BINARY,  priority = Priority.LOW)
    public abstract N minus(@NotNull N number, @NotNull N subtrahend);

    @ArithmeticMathematicalFunction(symbol = '*', description = "Multiplication of two numbers", type = Type.BINARY, priority = Priority.HIGH)
    public abstract N multiplyBy(@NotNull N number, @NotNull N multiplicand);

    @ArithmeticMathematicalFunction(symbol = '/', description = "Division of two numbers", type = Type.BINARY, priority = Priority.HIGH)
    public abstract N divideBy(@NotNull N number, @NotNull N divisor);

}

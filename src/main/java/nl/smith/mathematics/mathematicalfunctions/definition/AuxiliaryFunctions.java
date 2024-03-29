package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsBetween;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;

import javax.validation.constraints.NotNull;

@MathematicalFunctionContainer(name = "Auxiliary functions", description = "Auxiliary methods: faculty")
public abstract class AuxiliaryFunctions<N extends Number, S extends AuxiliaryFunctions<N, S>> extends RecursiveFunctionContainer<N, S> {

    public AuxiliaryFunctions() {
        super(AuxiliaryFunctions.class);
    }

    //TODO Test implementations
    @MathematicalFunction(description = "Faculty of a number")
    public abstract N faculty(@NotNull
                              @IsNaturalNumber
                              @IsBetween(floor = "0", includingFloor = true, ceiling = "100", includingCeiling = true) N number);

}

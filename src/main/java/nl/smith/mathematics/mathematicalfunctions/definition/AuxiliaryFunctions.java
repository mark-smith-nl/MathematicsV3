package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsBetween;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsNaturalNumber;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

import javax.validation.constraints.NotNull;

public abstract class AuxiliaryFunctions<T extends Number, S extends AuxiliaryFunctions> extends FunctionContainer<T, S> {

    @Override
    public String getDescription() {
        return "Auxiliary methods: faculty";
    }

    public abstract T faculty(@NotNull(message = "The number '${validatedValue}' should be a natural number")
                              @IsNaturalNumber
                              @IsBetween(floor = "0", ceiling = "100.4") T number);
}

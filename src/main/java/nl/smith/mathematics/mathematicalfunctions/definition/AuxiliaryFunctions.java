package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

import javax.validation.constraints.NotNull;

public abstract class AuxiliaryFunctions<T extends Number, S extends AuxiliaryFunctions> extends FunctionContainer<T, S> {

    @Override
    public String getDescription() {
        return "Auxiliary methods: faculty";
    }

    public abstract T faculty(@NotNull(message = "No argument specified for faculty method") T number);
}

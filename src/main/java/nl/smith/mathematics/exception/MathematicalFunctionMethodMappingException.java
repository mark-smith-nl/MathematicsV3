package nl.smith.mathematics.exception;

import nl.smith.mathematics.mathematicalfunctions.MathematicalFunctionMethodMapping;

public class MathematicalFunctionMethodMappingException extends RuntimeException {


    private final MathematicalFunctionMethodMapping<? extends Number> mathematicalFunctionMethodMapping;

    private final Number[] arguments;

    public MathematicalFunctionMethodMappingException(String message, Exception e, MathematicalFunctionMethodMapping<? extends Number> mathematicalFunctionMethodMapping, Number... arguments) {
        super(message, e);
        this.mathematicalFunctionMethodMapping = mathematicalFunctionMethodMapping;
        this.arguments = arguments;
    }

    public MathematicalFunctionMethodMapping<? extends Number> getMathematicalFunctionMethodMapping() {
        return mathematicalFunctionMethodMapping;
    }

    public Number[] getArguments() {
        return arguments;
    }
}

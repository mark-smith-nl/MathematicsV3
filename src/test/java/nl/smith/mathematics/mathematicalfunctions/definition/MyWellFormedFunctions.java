package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

public abstract class MyWellFormedFunctions<T extends Number> extends FunctionContainer<T> {
    
    public abstract T myFunction(T number);
}

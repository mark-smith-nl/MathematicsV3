package nl.smith.mathematics.numbertype;

import java.math.BigInteger;

public abstract class ArithmeticFunctions<T extends Number> extends Number {

    public abstract T add(T augend) ;

    public abstract T subtract(T augend) ;

    public abstract T multiply(T multiplicand);

    public abstract T divide(T divisor);

    public abstract T negate();

    public abstract T abs();

    public abstract T[] divideAndRemainder(T divisor);

    public abstract boolean isNaturalNumber();
}

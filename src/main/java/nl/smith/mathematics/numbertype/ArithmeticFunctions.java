package nl.smith.mathematics.numbertype;

import java.math.BigInteger;

public abstract class ArithmeticFunctions<T extends Number> extends Number {

    //TODO Tests
    public abstract T add(long augend) ;
    //TODO Tests
    public abstract T add(BigInteger augend) ;
    //TODO Tests
    public abstract T add(T augend) ;
    //TODO Tests
    public abstract T subtract(long augend) ;
    //TODO Tests
    public abstract T subtract(BigInteger augend) ;
    //TODO Tests
    public abstract T subtract(T augend) ;
    //TODO Tests
    public abstract T multiply(long multiplicand);
    //TODO Tests
    public abstract T multiply(BigInteger multiplicand);
    //TODO Tests
    public abstract T multiply(T multiplicand);
    //TODO Tests
    public abstract T divide(long divisor);
    //TODO Tests
    public abstract T divide(BigInteger divisor);
    //TODO Tests
    public abstract T divide(T divisor);
    //TODO Tests
    public abstract T negate();
    //TODO Tests
    public abstract T abs();
    //TODO Tests
    public abstract T[] divideAndRemainder(T divisor);
    //TODO Tests
    public abstract boolean isNaturalNumber();
    //TODO Tests
    /**
     * Returns the signum function of this {@code BigDecimal}.
     *
     * @return -1, 0, or 1 as the value of this {@code BigDecimal}
     *         is negative, zero, or positive.
     */
    public abstract int signum();
}

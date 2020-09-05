package nl.smith.mathematics.numbertype;

import java.math.BigInteger;

public abstract class ArithmeticOperations<T extends Number> extends Number {

    public abstract T add(long augend) ;

    public abstract T add(BigInteger augend) ;

    public abstract T add(T augend) ;

    public abstract T subtract(long augend) ;

    public abstract T subtract(BigInteger augend) ;

    public abstract T subtract(T augend) ;

    public abstract T multiply(long multiplicand);

    public abstract T multiply(BigInteger multiplicand);

    public abstract T multiply(T multiplicand);

    public abstract T divide(long divisor);

    public abstract T divide(BigInteger divisor);

    public abstract T divide(T divisor);

    public abstract T negate();

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

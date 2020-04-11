package nl.smith.mathematics.numbertype;

public abstract class ArithmeticFunctions<T extends Number> extends Number {

    public abstract T add(T augend) ;

    public abstract T subtract(T augend) ;

    public abstract T multiply(T multiplicand);

    public abstract T divide(T divisor);

    public abstract T negate();

    public abstract T abs();

    public abstract T[] divideAndRemainder(T divisor);

    public abstract boolean isNaturalNumber();

    /**
     * Returns the signum function of this {@code BigDecimal}.
     *
     * @return -1, 0, or 1 as the value of this {@code BigDecimal}
     *         is negative, zero, or positive.
     */
    public abstract int signum();
}

package nl.smith.mathematics.numbertype;

import java.math.BigInteger;

public interface ArithmeticOperations<T extends Number> {

    abstract T add(long augend) ;

    abstract T add(BigInteger augend) ;

    abstract T add(T augend) ;

    abstract T subtract(long augend) ;

    abstract T subtract(BigInteger augend) ;

    abstract T subtract(T augend) ;

    abstract T multiply(long multiplicand);

    abstract T multiply(BigInteger multiplicand);

    abstract T multiply(T multiplicand);

    abstract T divide(long divisor);

    abstract T divide(BigInteger divisor);

    abstract T divide(T divisor);

    abstract T negate();

    abstract T abs();
    //TODO Tests
    abstract T[] divideAndRemainder(T divisor);
    //TODO Tests
    abstract boolean isNaturalNumber();
    //TODO Tests
    /**
     * Returns the signum function of this {@code BigDecimal}.
     *
     * @return -1, 0, or 1 as the value of this {@code BigDecimal}
     *         is negative, zero, or positive.
     */
    abstract int signum();

}

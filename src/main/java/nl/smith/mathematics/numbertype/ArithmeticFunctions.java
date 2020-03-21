package nl.smith.mathematics.numbertype;

import java.math.BigInteger;

public interface ArithmeticFunctions<T extends Number> {

    T add(T augend) ;

    T subtract(T augend) ;

    T multiply(T multiplicand);

    T divide(T divisor);

    T negate();

    T abs();

    T[] divideAndRemainder(T divisor);
}

package nl.smith.mathematics.numbertype;

public interface ArithmeticFunctions<T extends Number> {

    T add(T augend) ;

    T subtract(T augend) ;

    T multiply(T multiplicand);

    T divide(T divisor);

    T negate();

    T abs();

}

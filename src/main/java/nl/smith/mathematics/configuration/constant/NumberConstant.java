package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.util.NumberUtil;

import java.math.BigDecimal;
import java.sql.NClob;

/**
 * Default values for all implementing classes are specified in {@link NumberConstant#PROPERTY_FILE_NAME}.
 * The specified values are associated with the thread in which the value is set.
 * Setting and retrieval of a value can be done using static methods in the implementing class.
 * @param <T>
 */
public class NumberConstant<T extends Number> extends ConstantConfiguration<T>{

    public enum integerValueOf {
        Scale("nl.smith.mathematics.configuration.constant.Scale"),
        TaylorDegreeOfPolynom("nl.smith.mathematics.configuration.constant.TaylorDegreeOfPolynom");

        private final NumberConstant<Integer> numberConstant;

        private final Class<Integer> clazz;

        integerValueOf(String propertyName) {
            clazz = Integer.class;
            numberConstant = new NumberConstant<>(clazz, propertyName);
        }

        public Integer get() {
            return numberConstant.getValue();
        }

        // TODO Check if this is thread save.
        public void set(Integer number) {
            numberConstant.setValue(number);
        }

        public void set(String stringNumber) {
            numberConstant.setValue(NumberUtil.valueOf(stringNumber, clazz));
        }
    }

    public enum rationalValueOf {
        Pi("nl.smith.mathematics.configuration.constant.Pi"),
        Euler("nl.smith.mathematics.configuration.constant.Euler"),
        MaximumError("nl.smith.mathematics.configuration.constant.error");

        private final NumberConstant<RationalNumber> numberConstant;

        private final Class<RationalNumber> clazz;

        rationalValueOf(String propertyName) {
            clazz = RationalNumber.class;
            numberConstant = new NumberConstant<>(clazz, propertyName);
        }

        public RationalNumber get() {
            return numberConstant.getValue();
        }

        // TODO Check if this is thread save.
        public void set(RationalNumber number) {
            numberConstant.setValue(number);
        }

        public void set(String stringNumber) {
            numberConstant.setValue(NumberUtil.valueOf(stringNumber, clazz));
        }
    }

    public enum bigDecimalValueOf {
        Pi("nl.smith.mathematics.configuration.constant.Pi"),
        Euler("nl.smith.mathematics.configuration.constant.Euler"),
        MaximumError("nl.smith.mathematics.configuration.constant.error");

        private final NumberConstant<BigDecimal> numberConstant;

        private final Class<BigDecimal> clazz;

        bigDecimalValueOf(String propertyName) {
            clazz = BigDecimal.class;
            numberConstant = new NumberConstant<>(clazz, propertyName);
        }

        public BigDecimal get() {
            return numberConstant.getValue();
        }

        public void set(BigDecimal number) {
            numberConstant.setValue(number);
        }

        public void set(String stringNumber) {
            numberConstant.setValue(NumberUtil.valueOf(stringNumber, clazz));
        }
    }

    public NumberConstant(Class<T> clazz, String propertyName) {
        super(clazz, propertyName);
    }

}

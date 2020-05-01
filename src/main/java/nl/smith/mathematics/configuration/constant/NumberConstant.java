package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.util.NumberUtil;

import java.math.BigDecimal;

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

        integerValueOf(String propertyname) {
            clazz = Integer.class;
            numberConstant = new NumberConstant<Integer>(propertyname, clazz);
        }

        public Integer get() {
            return numberConstant.getValue();
        }

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

        rationalValueOf(String propertyname) {
            clazz = RationalNumber.class;
            numberConstant = new NumberConstant<RationalNumber>(propertyname, clazz);
        }

        public RationalNumber get() {
            return numberConstant.getValue();
        }

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

        bigDecimalValueOf(String propertyname) {
            clazz = BigDecimal.class;
            numberConstant = new NumberConstant<BigDecimal>(propertyname, clazz);
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

    public NumberConstant(String propertyName, Class<T> clazz) {
        super(propertyName, clazz);
    }

}

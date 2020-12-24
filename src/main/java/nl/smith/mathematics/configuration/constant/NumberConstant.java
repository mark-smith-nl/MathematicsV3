package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.numbertype.RationalNumber;

import java.math.BigDecimal;

/**
 * Default values for all implementing classes are specified in {@link NumberConstant#PROPERTY_FILE_NAME}.
 * The specified values are associated with the thread in which the value is set.
 * Setting and retrieval of a value can be done using static methods in the implementing class.
 *
 * @param <T>
 */
public class NumberConstant<T extends Number> extends ConstantConfiguration<T> {

    @Override
    public String constantDescription() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    public enum IntegerValueOf implements EnumConstant  {

        Scale("Number of decimals after the decimal point."),
        TaylorDegreeOfPolynom("Degree of the Taylor polynom.");

        private final NumberConstant<Integer> instance;

        private final Class<Integer> numberType = Integer.class;

        private final String description;

        IntegerValueOf(String description) {
            instance = new NumberConstant<>(numberType, this.getClass().getEnclosingClass().getCanonicalName() + "." + this.name());
            this.description = description;
        }

        public NumberConstant<Integer> value() {
            return instance;
        }

        @Override
        public String valueDescription() {
            return description;
        }

    }

    public enum BigDecimalValueOf implements EnumConstant {

        Pi("Ratio of circumference and diameter of a circle"),
        Euler("Constant of Napier"),
        MaximumError("Maximum error");

        private final NumberConstant<BigDecimal> instance;

        private final Class<BigDecimal> numberType = BigDecimal.class;

        private final String description;

        BigDecimalValueOf(String description) {
            instance = new NumberConstant<>(numberType, this.getClass().getEnclosingClass().getCanonicalName() + "." + this.name());
            this.description = description;
        }

        public NumberConstant<BigDecimal> value() {
            return instance;
        }

        @Override
        public String valueDescription() {
            return description;
        }

    }

    public enum RationalValueOf implements EnumConstant {

        Pi(BigDecimalValueOf.Pi.description),
        Euler(BigDecimalValueOf.Euler.description),
        MaximumError(BigDecimalValueOf.Pi.MaximumError.description);

        private final NumberConstant<RationalNumber> instance;

        private final Class<RationalNumber> numberType = RationalNumber.class;

        private final String description;

        RationalValueOf(String description) {
            instance = new NumberConstant<>(numberType, this.getClass().getEnclosingClass().getCanonicalName() + "." + this.name());
            this.description = description;
        }

        public NumberConstant<RationalNumber> value() {
            return instance;
        }

        @Override
        public String valueDescription() {
            return description;
        }

    }

    public NumberConstant(Class<T> numberType, String propertyName) {
        super(numberType, propertyName);
    }

}

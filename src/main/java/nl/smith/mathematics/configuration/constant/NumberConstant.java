package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.util.NumberUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default values for all implementing classes are specified in {@link NumberConstant#PROPERTY_FILE_NAME}.
 * The specified values are associated with the thread in which the value is set.
 * Setting and retrieval of a value can be done using static methods in the implementing class.
 *
 * @param <T>
 */
public class NumberConstant<T extends Number> extends ConstantConfiguration<T> {

    public enum integerValueOf implements ConfigurationConstant<Integer> {
        Scale("nl.smith.mathematics.configuration.constant.Scale"),
        TaylorDegreeOfPolynom("nl.smith.mathematics.configuration.constant.TaylorDegreeOfPolynom");

        private final NumberConstant<Integer> numberConstant;

        private final Class<Integer> numberType = Integer.class;

        integerValueOf(String propertyName) {
            numberConstant = new NumberConstant<>(numberType, propertyName);
        }

        public Integer get() {
            return numberConstant.getValue();
        }

        // TODO Check if this is thread save.
        public void set(Integer number) {
            numberConstant.setValue(number);
        }

        public void set(String stringNumber) {
            numberConstant.setValue(NumberUtil.valueOf(stringNumber, numberType));
        }

        @Override
        public Class<Integer> getNumberType() {
            return numberType;
        }
    }

    public enum rationalValueOf implements ConfigurationConstant<RationalNumber> {
        Pi("nl.smith.mathematics.configuration.constant.Pi"),
        Euler("nl.smith.mathematics.configuration.constant.Euler"),
        MaximumError("nl.smith.mathematics.configuration.constant.error");

        private final NumberConstant<RationalNumber> numberConstant;

        private final Class<RationalNumber> numberType = RationalNumber.class;

        rationalValueOf(String propertyName) {
            numberConstant = new NumberConstant<>(numberType, propertyName);
        }

        public RationalNumber get() {
            return numberConstant.getValue();
        }

        // TODO Check if this is thread save.
        public void set(RationalNumber number) {
            numberConstant.setValue(number);
        }

        public void set(String stringNumber) {
            numberConstant.setValue(NumberUtil.valueOf(stringNumber, numberType));
        }

        @Override
        public Class<RationalNumber> getNumberType() {
            return numberType;
        }
    }

    public enum bigDecimalValueOf implements ConfigurationConstant<BigDecimal> {
        Pi("nl.smith.mathematics.configuration.constant.Pi"),
        Euler("nl.smith.mathematics.configuration.constant.Euler"),
        MaximumError("nl.smith.mathematics.configuration.constant.error");

        private final NumberConstant<BigDecimal> numberConstant;

        private final Class<BigDecimal> numberClass = BigDecimal.class;

        bigDecimalValueOf(String propertyName) {
            numberConstant = new NumberConstant<>(numberClass, propertyName);
        }

        public BigDecimal get() {
            return numberConstant.getValue();
        }

        public void set(BigDecimal number) {
            numberConstant.setValue(number);
        }

        public void set(String stringNumber) {
            numberConstant.setValue(NumberUtil.valueOf(stringNumber, numberClass));
        }

        @Override
        public Class<BigDecimal> getNumberType() {
            return numberClass;
        }
    }

    interface ConfigurationConstant<N extends Number> {

        N get();

        void set(N number);

        void set(String stringNumber);

        public Class<N> getNumberType();
    }

    public NumberConstant(Class<T> numberType, String propertyName) {
        super(numberType, propertyName);
    }

    /**
     * Method returns a map of enum names and a set of classes they belong to
     *
     * @return Map of enum names as key and a tree set of Number classes as value.
     */
    //TODO Unit Test
    public static Map<String, Set<Class<? extends Number>>> getEnumsGroupedByName() {
        Map<String, Set<Class<? extends Number>>> enumsGroupedByName = new HashMap<>();

        Stream.of(NumberConstant.class.getDeclaredClasses())
                .filter(Class::isEnum)
                .flatMap(enumClass -> Arrays.stream(enumClass.getEnumConstants()).map(ec -> (ConfigurationConstant<?>) ec))
                .collect(Collectors.groupingBy(Object::toString))
                .forEach((enumName, enumValuesForName) -> {
                    Set<Class<? extends Number>> numberTypes = new TreeSet<>((o1, o2) -> o1.getSimpleName().compareTo(o2.getSimpleName()));
                    enumValuesForName.forEach(enumValue -> numberTypes.add(enumValue.getNumberType()));
                    enumsGroupedByName.put(enumName, numberTypes);
                });

        return enumsGroupedByName;
    }

}

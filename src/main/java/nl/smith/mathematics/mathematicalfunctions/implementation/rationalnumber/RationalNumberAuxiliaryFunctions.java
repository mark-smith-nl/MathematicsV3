package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.mathematicalfunctions.definition.AuxiliaryFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.context.annotation.Bean;

import static nl.smith.mathematics.numbertype.RationalNumber.ONE;

public class RationalNumberAuxiliaryFunctions extends AuxiliaryFunctions<RationalNumber, RationalNumberAuxiliaryFunctions> {

    private final static String SIBLING_BEAN_NAME = "RATIONALNUMBERAUXILIARYFUNCTIONS";

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    @Override
    public RationalNumberAuxiliaryFunctions makeSibling() {
        return new RationalNumberAuxiliaryFunctions();
    }

    @Override
    public RationalNumber faculty(RationalNumber number) {
        return number.compareTo(ONE) < 1 ? ONE : number.multiply(faculty(number.subtract(ONE)));
    }

}

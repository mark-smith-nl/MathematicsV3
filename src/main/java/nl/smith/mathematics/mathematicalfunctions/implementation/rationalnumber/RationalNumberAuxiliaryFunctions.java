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
        RationalNumber faculty = ONE;
        RationalNumber i = new RationalNumber(2);
        while (i.compareTo(number) < 1) {
            faculty = faculty.multiply(i);
            i = i.add(ONE);
        }
        return faculty;
    }

}

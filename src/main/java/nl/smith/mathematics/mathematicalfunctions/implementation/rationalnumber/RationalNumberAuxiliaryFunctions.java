package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.mathematicalfunctions.definition.AuxiliaryFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.service.MethodAnnotationFinderService;
import org.springframework.context.annotation.Bean;

import javax.validation.constraints.NotNull;

import static nl.smith.mathematics.numbertype.RationalNumber.ONE;

public class RationalNumberAuxiliaryFunctions extends AuxiliaryFunctions<RationalNumber, RationalNumberAuxiliaryFunctions> {

    private final static String SIBLING_BEAN_NAME = "RATIONALNUMBERAUXILIARYFUNCTIONS";

    @Override
    public RationalNumber faculty(RationalNumber number) {
        return number.compareTo(ONE) < 1 ? ONE : number.multiply(faculty(number.subtract(ONE)));
    }

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    public RationalNumberAuxiliaryFunctions makeSibling() {
        return new RationalNumberAuxiliaryFunctions();
    }

}

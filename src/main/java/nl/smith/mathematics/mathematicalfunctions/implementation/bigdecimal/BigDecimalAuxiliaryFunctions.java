package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.mathematicalfunctions.definition.AuxiliaryFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberAuxiliaryFunctions;
import org.springframework.context.annotation.Bean;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;

public class BigDecimalAuxiliaryFunctions extends AuxiliaryFunctions<BigDecimal, BigDecimalAuxiliaryFunctions> {

	private final static String SIBLING_BEAN_NAME = "BIGDECIMALAUXILIARYFUNCTIONS";

	@Override
	public BigDecimal faculty(BigDecimal number) {
		if (number.compareTo(ONE) < 1) {
			return ONE;
		}

		return number.multiply(number.subtract(ONE));
	}

	@Override
	public String getSiblingBeanName() {
		return SIBLING_BEAN_NAME;
	}

	@Bean(SIBLING_BEAN_NAME)
	public BigDecimalAuxiliaryFunctions makeSibling() {
		return new BigDecimalAuxiliaryFunctions();
	}

}

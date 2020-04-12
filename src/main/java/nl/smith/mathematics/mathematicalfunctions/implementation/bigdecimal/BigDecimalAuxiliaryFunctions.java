package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.mathematicalfunctions.definition.AuxiliaryFunctions;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;

public class BigDecimalAuxiliaryFunctions extends AuxiliaryFunctions<BigDecimal, BigDecimalAuxiliaryFunctions> {

	private final static String SIBLING_BEAN_NAME = "BIGDECIMALAUXILIARYFUNCTIONS";

	@Override
	public String getSiblingBeanName() {
		return SIBLING_BEAN_NAME;
	}

	@Bean(SIBLING_BEAN_NAME)
	@Override
	public BigDecimalAuxiliaryFunctions makeSibling() {
		return new BigDecimalAuxiliaryFunctions();
	}

	/** Non recursive method to calculate n!
	 * The method is not recursive to prevent a stack overflow.
	 */
	@Override
	public BigDecimal faculty(BigDecimal number) {
		 return number.compareTo(ONE) < 1 ? ONE : number.multiply(number.subtract(ONE));
	}

}

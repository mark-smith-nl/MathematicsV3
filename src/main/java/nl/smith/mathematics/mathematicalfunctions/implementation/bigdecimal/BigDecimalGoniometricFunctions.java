package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

public class BigDecimalGoniometricFunctions extends GoniometricFunctions<BigDecimal, BigDecimalGoniometricFunctions> {

	private final static String SIBLING_BEAN_NAME = "BIGDECIMALGONIOMETRICFUNCTIONS";

	public final static String name = "BigDecimalGoniometricFunctions";

	@Override
	public BigDecimal sin(BigDecimal angle) {
		return new BigDecimal(456);
	}

	@Override
	public BigDecimal cos(BigDecimal angle) {
		return new BigDecimal(456);
	}

	@Override
	public String getSiblingBeanName() {
		return SIBLING_BEAN_NAME;
	}

	@Bean(SIBLING_BEAN_NAME)
	public BigDecimalGoniometricFunctions makeSibling() {
		return new BigDecimalGoniometricFunctions();
	}

}

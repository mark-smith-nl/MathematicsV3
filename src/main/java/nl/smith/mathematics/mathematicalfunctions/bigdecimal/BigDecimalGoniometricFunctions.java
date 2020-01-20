package nl.smith.mathematics.mathematicalfunctions.bigdecimal;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;

@Component
public class BigDecimalGoniometricFunctions extends GoniometricFunctions<BigDecimal> {

	@Override
	public BigDecimal sin(BigDecimal angle) {
		return new BigDecimal(456);
	}

	@Override
	public BigDecimal cos(BigDecimal angle) {
		return new BigDecimal(456);
	}

}

package nl.smith.mathematics.functions.bigdecimal;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import nl.smith.mathematics.functions.definition.GoniometricFunctions;

@Component
public class BigDecimalGoniometricFunctions extends GoniometricFunctions<BigDecimal> {

	@Override
	public BigDecimal sin(BigDecimal angle) {
		return new BigDecimal(456);
	}

}

package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component(BigDecimalGoniometricFunctions.name)
public class BigDecimalGoniometricFunctions extends GoniometricFunctions<BigDecimal> {

	public final static String name = "BigDecimalGoniometricFunctions";

	@Override
	public BigDecimal sin(BigDecimal angle) {
		return new BigDecimal(456);
	}

	@Override
	public BigDecimal cos(BigDecimal angle) {
		return new BigDecimal(456);
	}

}

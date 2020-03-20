package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.mathematicalfunctions.definition.AuxiliaryFunctions;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;

public class BigDecimalAuxiliaryFunctions extends AuxiliaryFunctions<BigDecimal> {

	@Override
	public BigDecimal faculty(@NotNull BigDecimal number) {
		if (number.compareTo(ONE) < 1) {
			return ONE;
		}

		return number.multiply(number.subtract(ONE));

	}
}

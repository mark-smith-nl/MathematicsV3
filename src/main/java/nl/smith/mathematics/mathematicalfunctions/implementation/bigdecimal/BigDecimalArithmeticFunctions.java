package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.configuration.constant.RoundingMode;
import nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions;
import org.springframework.context.annotation.Bean;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static nl.smith.mathematics.configuration.constant.NumberConstant.integerValueOf.Scale;

public class BigDecimalArithmeticFunctions extends ArithmeticFunctions<BigDecimal, BigDecimalArithmeticFunctions> {

	private final static String SIBLING_BEAN_NAME = "BIG_DECIMAL_ARITHMETIC_FUNCTIONS";

	@Override
	public String getSiblingBeanName() {
		return SIBLING_BEAN_NAME;
	}

	@Bean(SIBLING_BEAN_NAME)
	@Override
	public BigDecimalArithmeticFunctions makeSibling() {
		return new BigDecimalArithmeticFunctions();
	}


	@Override
	public BigDecimal sum(@NotNull BigDecimal number, @NotNull BigDecimal augend) {
		return number.add(augend);
	}

	@Override
	public BigDecimal subtract(@NotNull BigDecimal number, @NotNull BigDecimal subtrahend) {
		return number.subtract(subtrahend);
	}

	@Override
	public BigDecimal multiply(@NotNull BigDecimal number, @NotNull BigDecimal multiplicand) {
		return number.multiply(multiplicand);
	}

	@Override
	public BigDecimal divide(@NotNull BigDecimal number, @NotNull BigDecimal divisor) {
		return number.divide(divisor, Scale.get(), RoundingMode.get());
	}

}

package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RoundingMode;
import nl.smith.mathematics.mathematicalfunctions.definition.ArithmeticFunctions;
import org.springframework.context.annotation.Bean;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static nl.smith.mathematics.configuration.constant.NumberConstant.IntegerValueOf.Scale;

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
	public BigDecimal minus(@NotNull BigDecimal number) {
		return number.negate();
	}

	@Override
	public BigDecimal plus(@NotNull BigDecimal number, @NotNull BigDecimal augend) {
		return number.add(augend);
	}

	@Override
	public BigDecimal minus(@NotNull BigDecimal number, @NotNull BigDecimal subtrahend) {
		return number.subtract(subtrahend);
	}

	@Override
	public BigDecimal multiplyBy(@NotNull BigDecimal number, @NotNull BigDecimal multiplicand) {
		return number.multiply(multiplicand);
	}

	@Override
	public BigDecimal divideBy(@NotNull BigDecimal number, @NotNull BigDecimal divisor) {
		return number.divide(divisor, Scale.value().get(), RoundingMode.value().get().mathRoundingMode());
	}

}

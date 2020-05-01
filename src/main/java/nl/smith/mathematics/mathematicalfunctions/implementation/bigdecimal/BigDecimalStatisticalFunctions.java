package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.configuration.constant.RoundingMode;
import nl.smith.mathematics.mathematicalfunctions.definition.StatisticalFunctions;
import nl.smith.mathematics.util.ObjectWrapper;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static nl.smith.mathematics.configuration.constant.NumberConstant.integerValueOf;

public class BigDecimalStatisticalFunctions extends StatisticalFunctions<BigDecimal, BigDecimalStatisticalFunctions> {

	private final static String SIBLING_BEAN_NAME = "BIGDECIMALSTATISTICALFUNCTIONS";

	@Override
	public String getSiblingBeanName() {
		return SIBLING_BEAN_NAME;
	}

	@Bean(SIBLING_BEAN_NAME)
	@Override
	public BigDecimalStatisticalFunctions makeSibling() {
		return new BigDecimalStatisticalFunctions();
	}

	@Override
	public BigDecimal sum(BigDecimal... numbers) {
		ObjectWrapper<BigDecimal> sum = new ObjectWrapper<>(ZERO);
		Stream.of(numbers).forEach(n -> sum.setValue(sum.getValue().add(n)));

		return sum.getValue();
	}

	@Override
	public BigDecimal prod(BigDecimal... numbers) {
		ObjectWrapper<BigDecimal> prod = new ObjectWrapper<>(ONE);
		Stream.of(numbers).forEach(n -> prod.setValue(prod.getValue().multiply(n)));

		return prod.getValue();
	}

	@Override
	public BigDecimal average(BigDecimal... numbers) {
		return sibling.sum(numbers).divide(new BigDecimal(numbers.length), integerValueOf.Scale.get(), RoundingMode.get());
	}

	@Override
	public BigDecimal deviation(BigDecimal ... numbers) {
		BigDecimal average = sibling.average(numbers);

		ObjectWrapper<BigDecimal> sum = new ObjectWrapper<>(ZERO);
		Stream.of(numbers).forEach(number -> {
			BigDecimal difference = number.subtract(average);
			sum.setValue(difference.multiply(difference).add(sum.getValue()));
		});

		return sum.getValue().divide(new BigDecimal(numbers.length), integerValueOf.Scale.get(), RoundingMode.get());
	}

}

package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.mathematicalfunctions.definition.StatisticalFunctions;
import nl.smith.mathematics.util.ObjectWrapper;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class BigDecimalStatisticalFunctions extends StatisticalFunctions<BigDecimal> {

	@Override
	public BigDecimal sum(BigDecimal... numbers) {
		ObjectWrapper<BigDecimal> sum = new ObjectWrapper<>(BigDecimal.ZERO);
		Stream.of(numbers).forEach(n -> sum.setValue(sum.getValue().add(n)));

		return sum.getValue();
	}

	@Override
	public BigDecimal prod(BigDecimal... numbers) {
		ObjectWrapper<BigDecimal> prod = new ObjectWrapper<>(BigDecimal.ONE);
		Stream.of(numbers).forEach(n -> prod.setValue(prod.getValue().multiply(n)));

		return prod.getValue();
	}

	@Override
	public BigDecimal deviation(BigDecimal ... numbers) {
		// TODO Auto-generated method stub
		System.out.println("===>" + numbers);
		return new BigDecimal(456);
	}

	@Override
	public BigDecimal mean(BigDecimal ... numbers) {
		ObjectWrapper<BigDecimal> sum = new ObjectWrapper<>(BigDecimal.ZERO);
		Stream.of(numbers).forEach(n -> sum.setValue(sum.getValue().add(n)));

		return sum.getValue().divide(BigDecimal.valueOf(numbers.length), 1);
	}
}

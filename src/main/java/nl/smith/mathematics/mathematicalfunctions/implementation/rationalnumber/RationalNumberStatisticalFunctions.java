package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.mathematicalfunctions.definition.StatisticalFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.util.ObjectWrapper;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

import static nl.smith.mathematics.numbertype.RationalNumber.ONE;
import static nl.smith.mathematics.numbertype.RationalNumber.ZERO;

public class RationalNumberStatisticalFunctions extends StatisticalFunctions<RationalNumber, RationalNumberStatisticalFunctions> {

	private final static String SIBLING_BEAN_NAME = "RATIONALNUMBERSTATISTICALFUNCTIONS";

	@Override
	public String getSiblingBeanName() {
		return SIBLING_BEAN_NAME;
	}

	@Bean(SIBLING_BEAN_NAME)
	@Override
	public RationalNumberStatisticalFunctions makeSibling() {
		return new RationalNumberStatisticalFunctions();
	}

	@Override
	public RationalNumber sum(RationalNumber... numbers) {
		ObjectWrapper<RationalNumber> sum = new ObjectWrapper<>(ZERO);
		Stream.of(numbers).forEach(n -> sum.setValue(sum.getValue().add(n)));

		return sum.getValue();
	}

	@Override
	public RationalNumber prod(RationalNumber... numbers) {
		ObjectWrapper<RationalNumber> prod = new ObjectWrapper<>(ONE);
		Stream.of(numbers).forEach(n -> prod.setValue(prod.getValue().multiply(n)));

		return prod.getValue();
	}

	@Override
	public RationalNumber average(RationalNumber... numbers) {
		return sibling.sum(numbers).divide(numbers.length);
	}

	@Override
	public RationalNumber deviation(RationalNumber ... numbers) {
		RationalNumber average = sibling.average(numbers);

		ObjectWrapper<RationalNumber> sum = new ObjectWrapper<>(ZERO);
		Stream.of(numbers).forEach(number -> {
			RationalNumber difference = number.subtract(average);
			sum.setValue(difference.multiply(difference).add(sum.getValue()));
		});

		return sum.getValue().divide(numbers.length);
	}

	@Override
	public RationalNumber[] square(RationalNumber... numbers) {
		return new RationalNumber[0];
	}
}

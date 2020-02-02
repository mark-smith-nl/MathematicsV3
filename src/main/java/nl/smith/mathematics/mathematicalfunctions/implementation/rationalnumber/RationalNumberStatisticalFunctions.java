package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.mathematicalfunctions.definition.StatisticalFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.util.ObjectWrapper;

import java.util.stream.Stream;

public class RationalNumberStatisticalFunctions extends StatisticalFunctions<RationalNumber> {

	@Override
	public RationalNumber sum(RationalNumber... numbers) {
		ObjectWrapper<RationalNumber> sum = new ObjectWrapper<>(RationalNumber.ZERO);
		Stream.of(numbers).forEach(n -> sum.setValue(sum.getValue().add(n)));

		return sum.getValue();
	}

	@Override
	public RationalNumber prod(RationalNumber... numbers) {
		return null;
	}

	@Override
	public RationalNumber deviation(RationalNumber ... numbers) {
		return null;
	}

	@Override
	public RationalNumber mean(RationalNumber ... numbers) {
		return null;
	}
}

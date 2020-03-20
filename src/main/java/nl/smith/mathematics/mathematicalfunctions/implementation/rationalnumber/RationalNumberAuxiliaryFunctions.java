package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.mathematicalfunctions.definition.AuxiliaryFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;

import javax.validation.constraints.NotNull;

import static nl.smith.mathematics.numbertype.RationalNumber.ONE;

public class RationalNumberAuxiliaryFunctions extends AuxiliaryFunctions<RationalNumber> {

	@Override
	public RationalNumber faculty(@NotNull RationalNumber number) {
		if (number.compareTo(ONE) < 1) {
			return ONE;
		}
		System.out.println(number.intValue());
		return number.multiply(faculty(number.subtract(ONE)));

	}
}

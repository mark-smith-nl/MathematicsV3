package nl.smith.mathematics.functions.bigdecimal;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

import nl.smith.mathematics.annotation.FunctionContainer;
import nl.smith.mathematics.functions.definition.StatisticalFunctions;

@FunctionContainer
public class BigDecimalStatisticalFunctions extends StatisticalFunctions<BigDecimal> {

	@Override
	public BigDecimal min(BigDecimal... numbers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal max(BigDecimal... numbers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal average(@NotEmpty(message = "A list of one or more numbers should be provided.") BigDecimal... numbers) {
		return numbers[0].add(BigDecimal.TEN);
	}

	@Override
	public BigDecimal deviation(BigDecimal... numbers) {
		// TODO Auto-generated method stub
		return null;
	}

}

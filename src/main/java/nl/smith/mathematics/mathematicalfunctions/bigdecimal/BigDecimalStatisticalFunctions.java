package nl.smith.mathematics.mathematicalfunctions.bigdecimal;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import nl.smith.mathematics.mathematicalfunctions.definition.StatisticalFunctions;

@Component
public class BigDecimalStatisticalFunctions extends StatisticalFunctions<BigDecimal> {

	@Override
	public BigDecimal deviation(BigDecimal numbers) {
		// TODO Auto-generated method stub
		System.out.println("===>" + numbers);
		return new BigDecimal(456);
	}

}

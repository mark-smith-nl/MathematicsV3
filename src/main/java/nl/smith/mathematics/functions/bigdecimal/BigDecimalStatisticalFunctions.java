package nl.smith.mathematics.functions.bigdecimal;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import nl.smith.mathematics.functions.definition.StatisticalFunctions;

@Component
@Validated
public class BigDecimalStatisticalFunctions extends StatisticalFunctions<BigDecimal> {

	@Override
	public BigDecimal deviation(@NotEmpty BigDecimal numbers) {
		// TODO Auto-generated method stub
		return new BigDecimal(456);
	}

}

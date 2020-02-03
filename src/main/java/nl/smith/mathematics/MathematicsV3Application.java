package nl.smith.mathematics;

import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalStatisticalFunctions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.math.BigDecimal;

@SpringBootApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
		classes = FunctionContainer.class))
public class MathematicsV3Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MathematicsV3Application.class, args);

		BigDecimalStatisticalFunctions functionContainer = context.getBean(BigDecimalStatisticalFunctions.class);

		try {
			functionContainer.deviation(BigDecimal.ZERO);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(7);
		}
	}

	@Bean
	public String getName() {
		return "Hello";
	}
}

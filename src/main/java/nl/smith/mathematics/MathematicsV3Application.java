package nl.smith.mathematics;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import nl.smith.mathematics.mathematicalfunctions.bigdecimal.BigDecimalStatisticalFunctions;

@SpringBootApplication
public class MathematicsV3Application {

	@Autowired
	private BigDecimalStatisticalFunctions functionContainer;

	/*
	 * public MathematicsV3Application(Set<Functions<? extends Number>> functionContainers) { this.functionContainers = functionContainers; }
	 */

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

}

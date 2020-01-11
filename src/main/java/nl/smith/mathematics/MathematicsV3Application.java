package nl.smith.mathematics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MathematicsV3Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MathematicsV3Application.class, args);

		// BigDecimalStatisticalFunctions bean = context.getBean(BigDecimalStatisticalFunctions.class);

		// System.out.println(bean.average(null));
	}

}

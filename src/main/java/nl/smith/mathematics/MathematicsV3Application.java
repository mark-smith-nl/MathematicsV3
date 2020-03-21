package nl.smith.mathematics;

import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.math.BigInteger;

@SpringBootApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
    classes = FunctionContainer.class))
public class MathematicsV3Application {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication
        .run(MathematicsV3Application.class, args);
  }

}

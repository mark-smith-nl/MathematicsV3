package nl.smith.mathematics;

import javax.validation.ConstraintViolationException;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;
import nl.smith.mathematics.service.MethodAnnotationFinderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import org.springframework.context.annotation.Scope;

@SpringBootApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
    classes = FunctionContainer.class))
public class MathematicsV3Application {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(MathematicsV3Application.class, args);
    MethodAnnotationFinderService methodAnnotationFinderService = context.getBean("methodAnnotationFinderService", MethodAnnotationFinderService.class);

    try {
      methodAnnotationFinderService.getParentMethods(null);
    } catch (ConstraintViolationException e) {
      System.out.println("Exception thrown 1");
    }

    try {
      methodAnnotationFinderService.getSibling().getParentMethods(null);
    } catch (ConstraintViolationException e) {
      System.out.println("Exception thrown 2");
    }

    try {
      methodAnnotationFinderService.getSibling().getSibling().getParentMethods(null);
    } catch (ConstraintViolationException e) {
      System.out.println("Exception thrown 3");
    }
  }


}

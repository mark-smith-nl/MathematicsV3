package nl.smith.mathematics;

import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
    classes = FunctionContainer.class))
public class MathematicsV3Application {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication
        .run(MathematicsV3Application.class, args);

    System.out.println("Started application 'MathematicsV3'");
    RationalNumber rationalNumber = new RationalNumber(1, 7);
    System.out.println("Example: " + rationalNumber);
    System.out.println("Have fun!");
  }

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  public LocalValidatorFactoryBean getValidator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }

}

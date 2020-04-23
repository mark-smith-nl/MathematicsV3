package nl.smith.mathematics;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.configuration.constant.Scale;
import nl.smith.mathematics.configuration.constant.TaylorDegreeOfPolynom;
import nl.smith.mathematics.configuration.constant.rationalnumber.Pi;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberGoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberLogarithmicFunctions;
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
        ConfigurableApplicationContext context = SpringApplication.run(MathematicsV3Application.class, args);

        System.out.println();
        System.out.println("Started application 'MathematicsV3'");
        System.out.println();
        System.out.println("Code is available at: https://github.com/mark-smith-nl/MathematicsV3");
        System.out.println("Docker image is runnable as: docker run msmith19650728/mathematicsv3");
        System.out.println();
        System.out.println("Example: " + new RationalNumber(1, 7));
        System.out.println("Example: " + new RationalNumber(2, 14));
        Scale.set(10);
        System.out.println("Example: " + new RationalNumber(2, 14));
        RationalNumberOutputType.set(RationalNumberOutputType.Type.COMPONENTS_AND_EXACT);
        System.out.println("Example 2/14 * 1/3: " + (new RationalNumber(2, 14)).multiply(new RationalNumber(1, 3)));
        System.out.println("Example create number from string literal 12.345{6789}R: " + RationalNumber.valueOf("12.345{6789}R"));
        System.out.println();

        RationalNumberGoniometricFunctions rationalNumberGoniometricFunctions = context.getBean("rationalNumberGoniometricFunctions", RationalNumberGoniometricFunctions.class);

        RationalNumberOutputType.set(RationalNumberOutputType.Type.TRUNCATED);
        Scale.set(150);
        System.out.println("Calculate sin(𝝅/4) using Taylor series:");
        RationalNumber piDividedByFour = Pi.get().divide(4);
        for (int i = 0; i < 20; i++) {
            TaylorDegreeOfPolynom.set(i);
            System.out.println("Taylor (" + TaylorDegreeOfPolynom.get() + "): " + rationalNumberGoniometricFunctions.sin(piDividedByFour));
        }
        System.out.println();

        RationalNumber piDividedBySix = Pi.get().divide(6);
        System.out.println("Calculate cos(𝝅/6) using Taylor series:");
        for (int i = 0; i < 20; i++) {
            TaylorDegreeOfPolynom.set(i);
            System.out.println("Taylor (" + TaylorDegreeOfPolynom.get() + "): " + rationalNumberGoniometricFunctions.cos(piDividedBySix));
        }
        System.out.println();

        RationalNumberOutputType.set(RationalNumberOutputType.Type.TRUNCATED);
        System.out.println("\uD835\uDF45:");
        for (int i = 0; i < 150; i++) {
            Scale.set(i);
            System.out.println(Pi.get());
        }
        System.out.println();

        RationalNumberLogarithmicFunctions rationalNumberGoniometricFunctions1 = context.getBean("rationalNumberLogarithmicFunctions", RationalNumberLogarithmicFunctions.class);
        RationalNumberOutputType.set(RationalNumberOutputType.Type.TRUNCATED);
        System.out.println("Calculate Eulers's number:");
        for (int i = 0; i < 150; i++) {
            Scale.set(i);
            System.out.println(rationalNumberGoniometricFunctions1.exp(RationalNumber.ONE));
        }
        System.out.println();

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

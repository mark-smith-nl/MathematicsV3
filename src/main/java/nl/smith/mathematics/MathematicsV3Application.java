package nl.smith.mathematics;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.configuration.constant.RoundingMode;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.definition.LogarithmicFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalGoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalStatisticalFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberGoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberLogarithmicFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.service.MethodRunnerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;

import static nl.smith.mathematics.configuration.constant.NumberConstant.bigDecimalValueOf;
import static nl.smith.mathematics.configuration.constant.NumberConstant.integerValueOf.Scale;
import static nl.smith.mathematics.configuration.constant.NumberConstant.integerValueOf.TaylorDegreeOfPolynom;
import static nl.smith.mathematics.configuration.constant.NumberConstant.rationalValueOf;
import static nl.smith.mathematics.configuration.constant.RationalNumberOutputType.Type;
import static nl.smith.mathematics.configuration.constant.RationalNumberOutputType.set;

@SpringBootApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {RecursiveFunctionContainer.class}))
public class MathematicsV3Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MathematicsV3Application.class, args);

        MethodRunnerService methodRunnerService = context.getBean(MethodRunnerService.class);
        methodRunnerService.setNumberType(RationalNumber.class);
        System.out.println(methodRunnerService.invokeMathematicalMethod("faculty", BigDecimal.valueOf(10)).getClass());
    }

    public static void main2(String[] args) {
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
        RationalNumberOutputType.set(Type.COMPONENTS_AND_EXACT);
        System.out.println("Example 2/14 * 1/3: " + (new RationalNumber(2, 14)).multiply(new RationalNumber(1, 3)));
        System.out.println("Example create number from string literal 12.345{6789}R: " + RationalNumber.valueOf("12.345{6789}R"));
        System.out.println();

        GoniometricFunctions goniometricFunctions = context.getBean("rationalNumberGoniometricFunctions", RationalNumberGoniometricFunctions.class);

        set(Type.TRUNCATED);
        Scale.set(150);
        System.out.println("Calculate sin(𝝅/4) using Taylor series:");
        RationalNumber piDividedByFour = rationalValueOf.Pi.get().divide(4);
        for (int i = 0; i < 20; i++) {
            TaylorDegreeOfPolynom.set(i);
            System.out.println("Taylor (" + TaylorDegreeOfPolynom.get() + "): " + goniometricFunctions.sin(piDividedByFour));
        }
        System.out.println();

        goniometricFunctions = context.getBean("bigDecimalGoniometricFunctions", BigDecimalGoniometricFunctions.class);
        BigDecimal piDividedBySix = bigDecimalValueOf.Pi.get().divide(new BigDecimal(6), Scale.get(), RoundingMode.get());
        System.out.println("Calculate cos(𝝅/6) using Taylor series:");
        for (int i = 0; i < 20; i++) {
            TaylorDegreeOfPolynom.set(i);
            System.out.println("Taylor (" + TaylorDegreeOfPolynom.get() + "): " + goniometricFunctions.cos(piDividedBySix));
        }
        System.out.println();

        RationalNumberOutputType.set(Type.TRUNCATED);
        System.out.println("\uD835\uDF45:");
        for (int i = 0; i < 150; i++) {
            Scale.set(i);
            System.out.println(rationalValueOf.Pi.get());
        }
        System.out.println();

        LogarithmicFunctions logarithmicFunctions = context.getBean("rationalNumberLogarithmicFunctions", RationalNumberLogarithmicFunctions.class);
        RationalNumberOutputType.set(Type.TRUNCATED);
        System.out.println("Calculate Eulers's number:");
        for (int i = 0; i < 150; i++) {
            Scale.set(i);
            System.out.println(logarithmicFunctions.exp(RationalNumber.ONE));
        }
        System.out.println();

        System.out.println("Have fun!");

        BigDecimalStatisticalFunctions bigDecimalStatisticalFunctions = context.getBean("bigDecimalStatisticalFunctions", BigDecimalStatisticalFunctions.class);
        BigDecimal[] numbers = new BigDecimal[]{new BigDecimal(2), new BigDecimal(4), new BigDecimal(5), new BigDecimal(5), new BigDecimal(6), new BigDecimal(7), new BigDecimal(9), new BigDecimal(10)};
        System.out.println("Sum: " + bigDecimalStatisticalFunctions.sum(numbers));
        System.out.println("Average: " + bigDecimalStatisticalFunctions.average(numbers));
        System.out.println("Deviation: " + bigDecimalStatisticalFunctions.deviation(numbers));
        Scale.set(3);
        RoundingMode.set(java.math.RoundingMode.CEILING);
        System.out.println(new BigDecimal(16).divide(new BigDecimal(3), Scale.get(), RoundingMode.get()));
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

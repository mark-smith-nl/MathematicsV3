package nl.smith.mathematics;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.configuration.constant.RoundingMode;
import nl.smith.mathematics.development.hva._smith.componentscan.byannotation.AnnotationForFilter;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.definition.LogarithmicFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalGoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalStatisticalFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberGoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberLogarithmicFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
@ComponentScan(includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {RecursiveFunctionContainer.class}),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = AnnotationForFilter.class)},
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*\\.development\\..*") // Ignore everything in the development package
)
public class MathematicsV3Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MathematicsV3Application.class);

    @Value("${server.port}")
    private String serverPort;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MathematicsV3Application.class, args);

        LOGGER.info("Started application 'MathematicsV3'");
        LOGGER.info("Code is available at: https://github.com/mark-smith-nl/MathematicsV3");
        LOGGER.info("Docker image is runnable as: docker run msmith19650728/mathematicsv3");
        LOGGER.info("Example: {}", new RationalNumber(1, 7));
        LOGGER.info("Example: {}", new RationalNumber(2, 14));
        Scale.set(10);
        LOGGER.info("Example: {}", new RationalNumber(2, 14));
        RationalNumberOutputType.set(Type.COMPONENTS_AND_EXACT);
        LOGGER.info("Example 2/14 * 1/3: {}", (new RationalNumber(2, 14)).multiply(new RationalNumber(1, 3)));
        LOGGER.info("Example create number from string literal 12.345[6789]R: {}", RationalNumber.valueOf("12.345[6789]R"));

        GoniometricFunctions<RationalNumber, ?> rationalNumberGoniometricFunctions = context.getBean("rationalNumberGoniometricFunctions", RationalNumberGoniometricFunctions.class);

        set(Type.TRUNCATED);
        Scale.set(150);
        LOGGER.info("Calculate sin(𝝅/4) using Taylor series:");
        RationalNumber piDividedByFour = rationalValueOf.Pi.get().divide(4);
        for (int i = 0; i < 20; i++) {
            TaylorDegreeOfPolynom.set(i);
            LOGGER.info("Taylor ({}): {}", TaylorDegreeOfPolynom.get(), rationalNumberGoniometricFunctions.sin(piDividedByFour));
        }

        GoniometricFunctions<BigDecimal, ?> bigDecimalGoniometricFunctions = context.getBean("bigDecimalGoniometricFunctions", BigDecimalGoniometricFunctions.class);

        BigDecimal piDividedBySix = bigDecimalValueOf.Pi.get().divide(new BigDecimal(6), Scale.get(), RoundingMode.get());
        LOGGER.info("Calculate cos(𝝅/6) using Taylor series:");
        for (int i = 0; i < 20; i++) {
            TaylorDegreeOfPolynom.set(i);
            LOGGER.info("Taylor ({}): {}", TaylorDegreeOfPolynom.get(), bigDecimalGoniometricFunctions.cos(piDividedBySix));
        }

        RationalNumberOutputType.set(Type.TRUNCATED);
        for (int i = 0; i < 150; i++) {
            Scale.set(i);
            LOGGER.info(rationalValueOf.Pi.get().toString());
        }

        LogarithmicFunctions<RationalNumber, ?> logarithmicFunctions = context.getBean("rationalNumberLogarithmicFunctions", RationalNumberLogarithmicFunctions.class);
        RationalNumberOutputType.set(Type.TRUNCATED);
        LOGGER.info("Calculate Eulers's number:");
        for (int i = 0; i < 150; i++) {
            Scale.set(i);
            LOGGER.info(logarithmicFunctions.exp(RationalNumber.ONE).toString());
        }

        LOGGER.info("Have fun!");

        BigDecimalStatisticalFunctions bigDecimalStatisticalFunctions = context.getBean("bigDecimalStatisticalFunctions", BigDecimalStatisticalFunctions.class);
        BigDecimal[] numbers = new BigDecimal[]{new BigDecimal(2), new BigDecimal(4), new BigDecimal(5), new BigDecimal(5), new BigDecimal(6), new BigDecimal(7), new BigDecimal(9), new BigDecimal(10)};
        LOGGER.info("Sum: {}", bigDecimalStatisticalFunctions.sum(numbers));
        LOGGER.info("Average: {}", bigDecimalStatisticalFunctions.average(numbers));
        LOGGER.info("Deviation: {}", bigDecimalStatisticalFunctions.deviation(numbers));
        Scale.set(3);
        RoundingMode.set(java.math.RoundingMode.CEILING);
        LOGGER.info(new BigDecimal(16).divide(new BigDecimal(3), Scale.get(), RoundingMode.get()).toString());

        MathematicsV3Application application = context.getBean(MathematicsV3Application.class);
        LOGGER.info("Aaaahhhhhhhhapplication url: http://localhost:{}", application.serverPort);
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

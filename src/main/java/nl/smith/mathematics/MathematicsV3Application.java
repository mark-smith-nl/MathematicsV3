package nl.smith.mathematics;

import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberOutputType;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RoundingMode;
import nl.smith.mathematics.controller.DefaultController;
import nl.smith.mathematics.controller.RequestHeaderFilter;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.definition.LogarithmicFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalGoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalStatisticalFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberGoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber.RationalNumberLogarithmicFunctions;
import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.service.MethodRunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;

import static nl.smith.mathematics.configuration.constant.NumberConstant.BigDecimalValueOf;
import static nl.smith.mathematics.configuration.constant.NumberConstant.IntegerValueOf.Scale;
import static nl.smith.mathematics.configuration.constant.NumberConstant.IntegerValueOf.TaylorDegreeOfPolynom;
import static nl.smith.mathematics.configuration.constant.NumberConstant.RationalValueOf;

@SpringBootApplication
@ComponentScan(includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {RecursiveFunctionContainer.class})},
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*\\.development\\..*") // Ignore everything in the development package
)
public class MathematicsV3Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MathematicsV3Application.class);

    @Value("${server.port}")
    private String serverPort;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MathematicsV3Application.class, args);
        if (args.length > 0) {
            LOGGER.info("Started application 'MathematicsV3'");
            LOGGER.info("Code is available at: https://github.com/mark-smith-nl/MathematicsV3");
            LOGGER.info("Docker image is runnable as: docker run msmith19650728/mathematicsv3");
            LOGGER.info("Example: {}", new RationalNumber(1, 7));
            LOGGER.info("Example: {}", new RationalNumber(2, 14));
            Scale.value().set(10);
            LOGGER.info("Example: {}", new RationalNumber(2, 14));
            RationalNumberOutputType.value().set(RationalNumberOutputType.PredefinedType.COMPONENTS_AND_EXACT);
            LOGGER.info("Example 2/14 * 1/3: {}", (new RationalNumber(2, 14)).multiply(new RationalNumber(1, 3)));
            LOGGER.info("Example create number from string literal 12.345[6789]R: {}", RationalNumber.valueOf("12.345[6789]R"));

            GoniometricFunctions<RationalNumber, ?> rationalNumberGoniometricFunctions = context.getBean("rationalNumberGoniometricFunctions", RationalNumberGoniometricFunctions.class);

            RationalNumberOutputType.value().set(RationalNumberOutputType.PredefinedType.TRUNCATED);
            Scale.value().set(150);
            LOGGER.info("Calculate sin(𝝅/4) using Taylor series:");
            RationalNumber piDividedByFour = RationalValueOf.Pi.value().get().divide(4);
            for (int i = 0; i < 20; i++) {
                TaylorDegreeOfPolynom.value().set(i);
                LOGGER.info("Taylor ({}): {}", TaylorDegreeOfPolynom.value().get(), rationalNumberGoniometricFunctions.sin(piDividedByFour));
            }

            GoniometricFunctions<BigDecimal, ?> bigDecimalGoniometricFunctions = context.getBean("bigDecimalGoniometricFunctions", BigDecimalGoniometricFunctions.class);

            BigDecimal piDividedBySix = BigDecimalValueOf.Pi.value().get().divide(new BigDecimal(6), Scale.value().get(), RoundingMode.value().get().mathRoundingMode());
            LOGGER.info("Calculate cos(𝝅/6) using Taylor series:");
            for (int i = 0; i < 20; i++) {
                TaylorDegreeOfPolynom.value().set(i);
                LOGGER.info("Taylor ({}): {}", TaylorDegreeOfPolynom.value().get(), bigDecimalGoniometricFunctions.cos(piDividedBySix));
            }

            RationalNumberOutputType.value().set(RationalNumberOutputType.PredefinedType.TRUNCATED);
            for (int i = 0; i < 150; i++) {
                Scale.value().set(i);
                LOGGER.info(RationalValueOf.Pi.value().get().toString());
            }

            LogarithmicFunctions<RationalNumber, ?> logarithmicFunctions = context.getBean("rationalNumberLogarithmicFunctions", RationalNumberLogarithmicFunctions.class);
            RationalNumberOutputType.value().set(RationalNumberOutputType.PredefinedType.TRUNCATED);
            LOGGER.info("Calculate Eulers's number:");
            for (int i = 0; i < 150; i++) {
                Scale.value().set(i);
                LOGGER.info(logarithmicFunctions.exp(RationalNumber.ONE).toString());
            }

            LOGGER.info("Have fun!");

            BigDecimalStatisticalFunctions bigDecimalStatisticalFunctions = context.getBean("bigDecimalStatisticalFunctions", BigDecimalStatisticalFunctions.class);
            BigDecimal[] numbers = new BigDecimal[]{new BigDecimal(2), new BigDecimal(4), new BigDecimal(5), new BigDecimal(5), new BigDecimal(6), new BigDecimal(7), new BigDecimal(9), new BigDecimal(10)};
            LOGGER.info("Sum: {}", bigDecimalStatisticalFunctions.sum(numbers));
            LOGGER.info("Average: {}", bigDecimalStatisticalFunctions.average(numbers));
            LOGGER.info("Deviation: {}", bigDecimalStatisticalFunctions.deviation(numbers));
            Scale.value().set(3);
            RoundingMode.value().set(RoundingMode.PredefinedType.CEILING);
            LOGGER.info(new BigDecimal(16).divide(new BigDecimal(3), Scale.value().get(), RoundingMode.value().get().mathRoundingMode()).toString());
        }
        MathematicsV3Application application = context.getBean(MathematicsV3Application.class);
        LOGGER.info("Application url: http://localhost:{}", application.serverPort);
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

    /**
     * Filter bean that filters function urls i.e. urls that begin with @{@link DefaultController#MAPPING_FUNCTION}
     *
     * @return Returns a filterbean.
     */
    @Bean
    public FilterRegistrationBean<RequestHeaderFilter> logFilter(MethodRunnerService methodRunnerService) {
        FilterRegistrationBean<RequestHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestHeaderFilter(methodRunnerService.getNumberTypes()));
        registrationBean.addUrlPatterns("/", DefaultController.MAPPING_FUNCTION + "/*");
        registrationBean.addUrlPatterns("/function/*");
        return registrationBean;
    }

}

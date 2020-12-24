package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.AngleType;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberNormalize;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberOutputType;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RoundingMode;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractConstantConfigurationTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @BeforeEach
    public void setUp() {
        System.setProperty(AngleType.class.getCanonicalName(), "RAD");
        System.setProperty(RationalNumberNormalize.class.getCanonicalName(), "YES");
        System.setProperty(RationalNumberOutputType.class.getCanonicalName(), "COMPONENTS");
        System.setProperty(RoundingMode.class.getCanonicalName(), "HALF_UP");

        logger.info("{}}: {}\n{}",
                AngleType.value().name(),
                AngleType.value().get(),
                AngleType.value().constantDescription());
        logger.info("{}}: {}\n{}",
                RationalNumberNormalize.value().name(),
                RationalNumberNormalize.value().get(),
                RationalNumberNormalize.value().constantDescription());
        logger.info("{}}: {}\n{}",
                RationalNumberOutputType.value().name(),
                RationalNumberOutputType.value().get(),
                RationalNumberOutputType.value().constantDescription());
        logger.info("{}}: {}\n{}",
                RoundingMode.value().name(),
                RoundingMode.value().get(),
                RoundingMode.value().constantDescription());
    }

}

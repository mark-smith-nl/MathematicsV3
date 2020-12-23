package nl.smith.mathematics.configuration.newconstant;

import nl.smith.mathematics.configuration.newconstant.EnumConstantConfiguration.AngleType;
import nl.smith.mathematics.configuration.newconstant.EnumConstantConfiguration.RationalNumberNormalize;
import nl.smith.mathematics.configuration.newconstant.EnumConstantConfiguration.RationalNumberOutputType;
import nl.smith.mathematics.configuration.newconstant.EnumConstantConfiguration.RoundingMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

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
                AngleType.value().description());
        logger.info("{}}: {}\n{}",
                RationalNumberNormalize.value().name(),
                RationalNumberNormalize.value().get(),
                RationalNumberNormalize.value().description());
        logger.info("{}}: {}\n{}",
                RationalNumberOutputType.value().name(),
                RationalNumberOutputType.value().get(),
                RationalNumberOutputType.value().description());
        logger.info("{}}: {}\n{}",
                RoundingMode.value().name(),
                RoundingMode.value().get(),
                RoundingMode.value().description());
    }

}

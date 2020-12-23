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

import static org.junit.jupiter.api.Assertions.*;

public class ConstantConfigurationTest extends AbstractConstantConfigurationTest {

    @Test
    public void getEnumConstantInstances() {
        Set<? extends Class<? extends EnumConstantConfiguration>> enumConstantClasses = ConstantConfiguration.getEnumConstantInstances().stream().map(inst -> inst.getClass()).collect(Collectors.toSet());
        assertEquals(4, enumConstantClasses.size());
        assertTrue(enumConstantClasses.contains(AngleType.class));
        assertTrue(enumConstantClasses.contains(RationalNumberNormalize.class));
        assertTrue(enumConstantClasses.contains(RationalNumberOutputType.class));
        assertTrue(enumConstantClasses.contains(RoundingMode.class));
    }
}

package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.AngleType;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberNormalize;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberOutputType;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RoundingMode;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

public class ConstantConfigurationTest extends AbstractConstantConfigurationTest{

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

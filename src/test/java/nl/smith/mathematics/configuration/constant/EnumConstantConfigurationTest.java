package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.AngleType;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberNormalize;
import nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberOutputType;
import nl.smith.mathematics.exception.StringToConstantConfigurationException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.AngleType.PredefinedType.DEG;
import static nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberNormalize.PredefinedType.*;
import static nl.smith.mathematics.configuration.constant.EnumConstantConfiguration.RationalNumberOutputType.PredefinedType.*;
import static org.junit.jupiter.api.Assertions.*;

class EnumConstantConfigurationTest {

    @Test
    public void angleType_invalidStringValue() throws StringToConstantConfigurationException {
        assertThrows(StringToConstantConfigurationException.class, () -> AngleType.value().set("deg"));
    }

    @Test
    public void angleType() throws StringToConstantConfigurationException {
        assertEquals("Angle", AngleType.value().name());
        assertEquals("Specification the way angles are specified.", AngleType.value().constantDescription());
        assertEquals(Set.of("DEG", "RAD", "GRAD"), AngleType.value().valuesAsString());
        AngleType.value().set(DEG);
        assertEquals(DEG, AngleType.value().get());
        AngleType.value().set("DEG");
        assertEquals(DEG, AngleType.value().get());
    }

    @Test
    public void rationalNumberNormalize() throws StringToConstantConfigurationException {
        assertEquals("Normalisation", RationalNumberNormalize.value().name());
        assertEquals("Specification whether rational numbers are normalized.", RationalNumberNormalize.value().constantDescription());
        assertEquals(Set.of("NO", "YES"), RationalNumberNormalize.value().valuesAsString());
        RationalNumberNormalize.value().set(YES);
        assertEquals(YES, RationalNumberNormalize.value().get());
        RationalNumberNormalize.value().set("NO");
        assertEquals(NO, RationalNumberNormalize.value().get());
    }

    @Test
    public void rationalNumberOutputType() throws StringToConstantConfigurationException {
        assertEquals("Output", RationalNumberOutputType.value().name());
        assertEquals("Specification how rational numbers are displayed.", RationalNumberOutputType.value().constantDescription());
        assertEquals(Set.of("ALL", "COMPONENTS", "COMPONENTS_AND_EXACT", "EXACT", "TRUNCATED"), RationalNumberOutputType.value().valuesAsString());
        RationalNumberOutputType.value().set(EXACT);
        assertEquals(EXACT, RationalNumberOutputType.value().get());
        RationalNumberOutputType.value().set("TRUNCATED");
        assertEquals(TRUNCATED, RationalNumberOutputType.value().get());
    }
}

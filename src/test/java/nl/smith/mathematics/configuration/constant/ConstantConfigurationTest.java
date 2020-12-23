package nl.smith.mathematics.configuration.constant;

import nl.smith.mathematics.exception.StringToConstantConfigurationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.math.RoundingMode.CEILING;
import static java.math.RoundingMode.HALF_UP;
import static nl.smith.mathematics.configuration.constant.AngleType.PredefinedType.DEG;
import static nl.smith.mathematics.configuration.constant.AngleType.PredefinedType.RAD;
import static nl.smith.mathematics.configuration.constant.RationalNumberNormalize.PredefinedType.YES;
import static nl.smith.mathematics.configuration.constant.RationalNumberOutputType.PredefinedType.COMPONENTS;
import static nl.smith.mathematics.configuration.constant.RationalNumberOutputType.PredefinedType.EXACT;
import static org.junit.jupiter.api.Assertions.*;

public class ConstantConfigurationTest {

    @BeforeEach
    public void setUp() {
        AngleType.set(RAD);
        RationalNumberNormalize.set(YES);
        RationalNumberOutputType.set(EXACT);
        RoundingMode.set(HALF_UP);
    }

    @Test
    public void getEnumConstants() {
        List<?> enumConstants = ConstantConfiguration.getEnumConstants();
        int expectedNumberOfEnumConstants = 4;
        assertEquals(expectedNumberOfEnumConstants, enumConstants.size(), format("Expected %d enum constants but fount %d.", expectedNumberOfEnumConstants, enumConstants.size()));
    }

    @Test
    public void values_AngleType() {
        Class<? extends ConstantConfiguration<?>> clazz = AngleType.class;
        Set<String> values = ConstantConfiguration.values(clazz);
        int expectedNumberOfValues = 3;
        assertEquals(expectedNumberOfValues, values.size(), format("Expected %d enum values for constant %s but found %d.", expectedNumberOfValues, clazz.getSimpleName(), values.size()));
    }

    @Test
    public void values_RationalNumberNormalize() {
        Class<? extends ConstantConfiguration<?>> clazz = RationalNumberNormalize.class;
        Set<String> values = ConstantConfiguration.values(clazz);
        int expectedNumberOfValues = 2;
        assertEquals(expectedNumberOfValues, values.size(), format("Expected %d enum values for constant %s but found %d.", expectedNumberOfValues, clazz.getSimpleName(), values.size()));
    }

    @Test
    public void values_RoundingMode() {
        Class<? extends ConstantConfiguration<?>> clazz = RoundingMode.class;
        Set<String> values = ConstantConfiguration.values(clazz);
        int expectedNumberOfValues = 8;
        assertEquals(expectedNumberOfValues, values.size(), format("Expected %d enum values for constant %s but found %d.", expectedNumberOfValues, clazz.getSimpleName(), values.size()));
    }

    @Test
    public void values_RationalNumberOutputType() {
        Class<? extends ConstantConfiguration<?>> clazz = RationalNumberOutputType.class;
        Set<String> values = ConstantConfiguration.values(clazz);
        int expectedNumberOfValues = 5;
        assertEquals(expectedNumberOfValues, values.size(), format("Expected %d enum values for constant %s but found %d.", expectedNumberOfValues, clazz.getSimpleName(), values.size()));
    }

    @Test
    public void setValue_AngleType() throws StringToConstantConfigurationException {
        Class<? extends ConstantConfiguration<?>> clazz = AngleType.class;
        ConstantConfiguration.setValue(clazz, "DEG");
        assertEquals(DEG.toString(), ConstantConfiguration.getValue(clazz));
    }

    @Test
    public void setValue_AngleType_error() {
        Class<? extends ConstantConfiguration<?>> clazz = AngleType.class;
        Assertions.assertThrows(StringToConstantConfigurationException.class, () -> ConstantConfiguration.setValue(clazz, "wrongValue"));
    }

    @Test
    public void setValue_RationalNumberNormalize() throws StringToConstantConfigurationException {
        Class<? extends ConstantConfiguration<?>> clazz = RationalNumberNormalize.class;
        ConstantConfiguration.setValue(clazz, "YES");
        assertEquals(YES.toString(), ConstantConfiguration.getValue(clazz));
    }

    @Test
    public void setValue_RationalNumberNormalize_error() {
        Class<? extends ConstantConfiguration<?>> clazz = RationalNumberNormalize.class;
        Assertions.assertThrows(StringToConstantConfigurationException.class, () -> ConstantConfiguration.setValue(clazz, "wrongValue"));
    }

    @Test
    public void setValue_RationalNumberOutputType() throws StringToConstantConfigurationException {
        Class<? extends ConstantConfiguration<?>> clazz = RationalNumberOutputType.class;
        ConstantConfiguration.setValue(clazz, "COMPONENTS");
        assertEquals(COMPONENTS.toString(), ConstantConfiguration.getValue(clazz));
    }

    @Test
    public void setValue_RationalNumberOutputType_error() {
        Class<? extends ConstantConfiguration<?>> clazz = RationalNumberOutputType.class;
        Assertions.assertThrows(StringToConstantConfigurationException.class, () -> ConstantConfiguration.setValue(clazz, "wrongValue"));
    }

    @Test
    public void setValue_RoundingMode() throws StringToConstantConfigurationException {
        Class<? extends ConstantConfiguration<?>> clazz = RoundingMode.class;
        ConstantConfiguration.setValue(clazz, "CEILING");
        assertEquals(CEILING.toString(), ConstantConfiguration.getValue(clazz));
    }

    @Test
    public void setValue_RoundingMode_error() {
        Class<? extends ConstantConfiguration<?>> clazz = RoundingMode.class;
        Assertions.assertThrows(StringToConstantConfigurationException.class, () -> ConstantConfiguration.setValue(clazz, "wrongValue"));
    }

    @Test
    public void getDescription_AngleType() {
        Class<? extends ConstantConfiguration<?>> clazz = AngleType.class;
        assertEquals("Specification the way angles are specified.", ConstantConfiguration.getDescription(clazz));
    }

    @Test
    public void getDescription_RationalNumberNormalize() {
        Class<? extends ConstantConfiguration<?>> clazz = RationalNumberNormalize.class;
        assertEquals("Specification whether rational numbers are normalized.", ConstantConfiguration.getDescription(clazz));
    }

    @Test
    public void getDescription_RationalNumberOutputType() {
        Class<? extends ConstantConfiguration<?>> clazz = RationalNumberOutputType.class;
        assertEquals("Specification how rational numbers are displayed.", ConstantConfiguration.getDescription(clazz));
    }

    @Test
    public void getDescription_RoundingMode() {
        Class<? extends ConstantConfiguration<?>> clazz = RoundingMode.class;
        assertEquals("Specification how numbers are rounded.", ConstantConfiguration.getDescription(clazz));
    }
}

package nl.smith.mathematics.service;

import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class MethodRunnerServiceTest {

    private final MethodRunnerService methodRunnerService;

    @Autowired
    public MethodRunnerServiceTest(MethodRunnerService methodRunnerService) {
        this.methodRunnerService = methodRunnerService;
    }

    @Test
    public void getNumberTypes() {
        assertEquals(new HashSet<>(Arrays.asList(BigDecimal.class, RationalNumber.class)), methodRunnerService.getNumberTypes());
    }

    @Test
    public void getNumberType() {
        assertNull(methodRunnerService.getNumberType());
    }
}
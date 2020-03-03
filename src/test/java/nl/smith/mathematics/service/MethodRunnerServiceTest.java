package nl.smith.mathematics.service;

import nl.smith.mathematics.mathematicalfunctions.implementation.BigDecimalTestFunctionContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MethodRunnerServiceTest {

    private final MethodRunnerService methodRunnerService;

    @Autowired
    public MethodRunnerServiceTest(MethodRunnerService methodRunnerService) {
        this.methodRunnerService = methodRunnerService;
    }

    @Test
    public void getMethods() {
        List<MethodSignature> methodSignatures = new ArrayList<>();
        methodRunnerService.extractAnnotatedMethodNames(BigDecimalTestFunctionContainer.class, methodSignatures);
        methodSignatures.forEach(System.out::println);
        assertEquals(3, methodSignatures.size());

    }
}
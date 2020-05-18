package nl.smith.mathematics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MethodRunnerServiceTest {

    private final MethodRunnerService methodRunnerService;

    @Autowired
    public MethodRunnerServiceTest(MethodRunnerService methodRunnerService) {
        this.methodRunnerService = methodRunnerService;
    }

}
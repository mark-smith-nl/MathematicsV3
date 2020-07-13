package nl.smith.mathematics.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ExpressionDigestionServiceTest {

    private final ExpressionDigestionService expressionDigestionService;

    @Autowired
    public ExpressionDigestionServiceTest(ExpressionDigestionService expressionDigestionService) {
        this.expressionDigestionService = expressionDigestionService;
    }

    @Test
    public void exists() {
        assertNotNull(expressionDigestionService);
    }


}
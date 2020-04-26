package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class FunctionContainerTest<S extends FunctionContainer<?, ?>> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Map<String, S> functionContainerAndSiblingBeans;

    // System under test (SUT)
    protected S functionContainer() {
        return functionContainerAndSiblingBeans.values().stream().findFirst().get();
    }

    @BeforeEach
    public void init() {
        RationalNumberOutputType.Type outputType = RationalNumberOutputType.Type.COMPONENTS;
        logger.info("Setting rational number output type to {} ({})", outputType.name(), outputType.getDescription());
        RationalNumberOutputType.set(outputType);
    }
}
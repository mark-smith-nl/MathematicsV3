package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.ParameterizedType;

@SpringBootTest
public class FunctionContainerTest<S extends FunctionContainer<?, ?>> {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ApplicationContext context;

    // System under test (SUT)
    protected S functionContainer;

    private void getFunctionContainerFromContext() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class functionContainerClass = (Class) genericSuperclass.getActualTypeArguments()[0];
        functionContainer = (S) context.getBean(functionContainerClass.getSimpleName().toUpperCase());
    }

    @BeforeEach
    public void init() {
        getFunctionContainerFromContext();
        RationalNumberOutputType.Type outputType = RationalNumberOutputType.Type.COMPONENTS;
        LOGGER.info("Setting rational number output type to {} ({})", outputType.name(), outputType.getDescription());
        RationalNumberOutputType.set(outputType);
    }
}
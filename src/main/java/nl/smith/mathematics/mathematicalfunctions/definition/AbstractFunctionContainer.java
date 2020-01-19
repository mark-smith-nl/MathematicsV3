package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Validated @MathematicalFunctions (name = "Unnamed functionset", description = "There is no description of this set of mathematical functions") public abstract class AbstractFunctionContainer<T extends Number> {

  private final static Logger LOGGER = LoggerFactory.getLogger(AbstractFunctionContainer.class);

  private Class<T> numberType;

  public Class<T> getNumberType() {
    return numberType;
  }

  @PostConstruct
  private void setNumberType() {
    Class<?> functionContainerClazz = this.getClass();

    if (functionContainerClazz.getAnnotation(MathematicalFunctions.class) == AbstractFunctionContainer.class.getAnnotation(MathematicalFunctions.class)) {
      LOGGER.warn("Please annotate your {} subclass with the {} annotation describing the name and context of your mathematical function set.",
        AbstractFunctionContainer.class.getCanonicalName(), MathematicalFunctions.class.getCanonicalName());
    }

    Type genericInterfaceClazz = functionContainerClazz.getGenericSuperclass();
    Class<?> functionContainerSuperClazz = functionContainerClazz.getSuperclass();

    numberType = (Class<T>) ((ParameterizedType) genericInterfaceClazz).getActualTypeArguments()[0];
  }
}

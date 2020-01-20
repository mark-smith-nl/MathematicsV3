package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Validated @MathematicalFunctions (name = "Unnamed functionset", description = "There is no description of this set of mathematical functions") public abstract class AbstractFunctionContainer<T extends Number> {

  private final static Logger LOGGER = LoggerFactory.getLogger(AbstractFunctionContainer.class);

  private Class<T> numberType;

  private Set<Signature> signatures = new HashSet<>();

  public Class<T> getNumberType() {
    return numberType;
  }

  @PostConstruct
  private void postConstruct() {
    setNumberType();
    setSignatures();
  }

  private void setNumberType() {
    Class<?> functionContainerClazz = this.getClass();

    if (functionContainerClazz.getAnnotation(MathematicalFunctions.class) == AbstractFunctionContainer.class.getAnnotation(MathematicalFunctions.class)) {
      LOGGER.warn("Please annotate your {} subclass with the {} annotation describing the name and context of your mathematical function set.",
        AbstractFunctionContainer.class.getCanonicalName(), MathematicalFunctions.class.getCanonicalName());
    }

    Type genericInterfaceClazz = functionContainerClazz.getGenericSuperclass();

    numberType = (Class<T>) ((ParameterizedType) genericInterfaceClazz).getActualTypeArguments()[0];
  }

  private void setSignatures() {
    Class<?> functionContainerClazz = this.getClass();
    Class<?> functionContainerSuperClazz = functionContainerClazz.getSuperclass();

    // @formatter:off
    Set<Method> mathematicalFunctions = Arrays.stream(functionContainerSuperClazz.getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(MathematicalFunction.class))
       // .filter(m -> Modifier.isAbstract(m.getModifiers()))
        //.filter(m -> m.getReturnType() == numberType)
        //.filter(m -> m.getParameterCount() > 0)
        .collect(Collectors.toSet());
    // @formatter:on

    mathematicalFunctions.forEach(m -> {
      signatures.add(new Signature(m.getAnnotation(MathematicalFunction.class), m.getName(), m, m.getParameterCount()));
    });

    System.out.println("===>" + signatures.size());
  }

}

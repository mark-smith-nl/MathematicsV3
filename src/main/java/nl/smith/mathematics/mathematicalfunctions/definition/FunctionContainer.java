package nl.smith.mathematics.mathematicalfunctions.definition;

import nl.smith.mathematics.annotation.MathematicalFunctions;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Validated @MathematicalFunctions (description = "There is no description of this set of mathematical functions")
public abstract class FunctionContainer<T extends Number> {

  private Class<T> numberType;

  public Class<T> getNumberType() {
    return numberType;
  }

  @PostConstruct
  private void setNumberType() {
    Class<?> functionContainerClazz = this.getClass();

    Type genericInterfaceClazz = functionContainerClazz.getGenericSuperclass();
    Class<?> functionContainerSuperClazz = functionContainerClazz.getSuperclass();

    numberType = (Class<T>) ((ParameterizedType) genericInterfaceClazz).getActualTypeArguments()[0];
  }
}

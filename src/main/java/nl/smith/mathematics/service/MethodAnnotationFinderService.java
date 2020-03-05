package nl.smith.mathematics.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import nl.smith.mathematics.annotation.IsPublicStaticInstanceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/** Service that tries to receives a specified annotation from a method, its supermethod(s) or bridge method. */
@Service
@Validated
public class MethodAnnotationFinderService {

  private final static Logger LOGGER = LoggerFactory.getLogger(MethodAnnotationFinderService.class);

  public <T extends Annotation> T getAnnotation(@NotNull Method method, @NotNull Class<T> annotationClass) {
    T annotation = method.getAnnotation(annotationClass);

    if (annotation == null) {
      Method bridgeMethod = getBridgeMethod(method);
      /*if (bridgeMethod == null) {

        List<Method> parentMethods = getParentMethods(method);
        parentMethods.entrySet().stream().filter()
      }*/
    }


    return annotation;
  }

  /** Returns a list of methods declared in the specified methods superclasses with the same signature as the specified method. */
  public List<Map.Entry<Class, Method>> getParentMethods(@NotNull(message = "No method specified") @IsPublicStaticInstanceMethod Method method) {
    List<Map.Entry<Class, Method>> parentMethods = new ArrayList<>();
    String name = method.getName();
    Class<?>[] parameterTypes = method.getParameterTypes();
    String methodSignature = name + "(" + Stream.of(parameterTypes).map(Class::getCanonicalName).collect(Collectors.joining(",")) + ")";
    Class<?> clazz = method.getDeclaringClass().getSuperclass();
    while (clazz != null) {
      try {
        LOGGER.info("Retrieving method: {}.{} not found.", clazz.getCanonicalName(), methodSignature);
        Method parentMethod = clazz.getDeclaredMethod(name, parameterTypes);
        parentMethods.add(new SimpleEntry(clazz, parentMethod));
      } catch (NoSuchMethodException e) {
        LOGGER.info("Method not found: {}.{} not found.", clazz.getCanonicalName(), methodSignature);
      } finally {
        clazz = clazz.getSuperclass();
      }
    }
    return parentMethods;
  }



  public Method getBridgeMethod(@NotNull Method method) {
    Class<?> clazz = method.getDeclaringClass();
    Method[] declaredMethods = clazz.getDeclaredMethods();
    Optional<Method> bridgedMethod = Stream.of(clazz.getDeclaredMethods())
        .filter(Method::isBridge)
        .map(BridgeMethodResolver::findBridgedMethod)
        .filter(m -> m.equals(method))
        .findFirst();
    
    return bridgedMethod.orElse(null);
  }
}

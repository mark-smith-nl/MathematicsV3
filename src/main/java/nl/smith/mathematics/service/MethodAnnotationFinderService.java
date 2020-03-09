package nl.smith.mathematics.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import nl.smith.mathematics.annotation.HasRecursiveValidatedMethods;
import nl.smith.mathematics.annotation.IsPublicInstanceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/** Service that tries to receive a specified annotation from a method, its supermethod(s) or bridge method. */
@Validated
@Service
@HasRecursiveValidatedMethods
@Primary
public class MethodAnnotationFinderService {

  private final static Logger LOGGER = LoggerFactory.getLogger(MethodAnnotationFinderService.class);

  private MethodAnnotationFinderService sibling;

  public MethodAnnotationFinderService getSibling() {
    return sibling;
  }

  public void setSibling(MethodAnnotationFinderService sibling) {
    this.sibling = sibling;
  }
  
  public <T extends Annotation> T getAnnotation(@NotNull @IsPublicInstanceMethod Method method, @NotNull Class<T> annotationClass) {
    T annotation = method.getAnnotation(annotationClass);

    if (annotation == null) {
      Method bridgeMethod = getBridgeMethod(method);
      if (bridgeMethod == null) {
        return getParentMethods(method).stream()
            .map(m -> m.getAnnotation(annotationClass))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
      } else {
        return getAnnotation(bridgeMethod, annotationClass);
      }
    }

    return annotation;
  }

  /** Returns a list of methods declared in the specified methods superclasses with the same signature as the specified method. */
  public List<Method> getParentMethods(@NotNull(message = "No method specified") @IsPublicInstanceMethod Method method) {
    List<Method> parentMethods = new ArrayList<>();
    String name = method.getName();
    Class<?>[] parameterTypes = method.getParameterTypes();
    String methodSignature = name + "(" + Stream.of(parameterTypes).map(Class::getCanonicalName).collect(Collectors.joining(",")) + ")";
    Class<?> clazz = method.getDeclaringClass().getSuperclass();
    while (clazz != null) {
      try {
        LOGGER.info("Retrieving method: {}.{} not found.", clazz.getCanonicalName(), methodSignature);
        Method parentMethod = clazz.getDeclaredMethod(name, parameterTypes);
        parentMethods.add(parentMethod);
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
    return Stream.of(clazz.getDeclaredMethods())
        .filter(Method::isBridge)
        .map(BridgeMethodResolver::findBridgedMethod)
        .filter(m -> m.equals(method))
        .findFirst().orElse(null);
  }

  @Bean("sibling")
  @Scope("prototype")
  public MethodAnnotationFinderService getMethodAnnotationFinderService() {
    return new MethodAnnotationFinderService();
  }
}

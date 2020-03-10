package nl.smith.mathematics.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import nl.smith.mathematics.annotation.IsPublicInstanceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.stereotype.Service;

/** Service that tries to receive a specified annotation from a method, its supermethod(s) or bridge method. */
@Service
@Primary
public class MethodAnnotationFinderService extends
    RecursiveValidatedService<MethodAnnotationFinderService> {

  private final static String SIBLING_BEAN_NAME = "METHODANNOTATIONFINDERSERVICE";

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
        logger.info("Retrieving method: {}.{} not found.", clazz.getCanonicalName(), methodSignature);
        Method parentMethod = clazz.getDeclaredMethod(name, parameterTypes);
        parentMethods.add(parentMethod);
      } catch (NoSuchMethodException e) {
        logger.info("Method not found: {}.{} not found.", clazz.getCanonicalName(), methodSignature);
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

  @Override
  public String getSiblingBeanName() {
    return SIBLING_BEAN_NAME;
  }

  @Bean(SIBLING_BEAN_NAME)
  public MethodAnnotationFinderService makeSibling() {
    return new MethodAnnotationFinderService();
  }

  public static void main(String[] args) {
    List<String> list = Arrays.asList("Mark", "Tom");
    list.f
  }
}

package nl.smith.mathematics.service;

import nl.smith.mathematics.annotation.constraint.IsPublicInstanceMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Service that tries to receive a specified annotation from a method, its supermethod(s) or bridge method. */
@Service
@Primary
public class MethodAnnotationFinderService extends
    RecursiveValidatedService<MethodAnnotationFinderService> {

  private final static String SIBLING_BEAN_NAME = "METHODANNOTATIONFINDERSERVICE";

  /**
   *
   * @param method The method to inspect if an annotation is available
   * @param annotationClass The annotation class
   * @param <T>
   * @return
   */
  public <T extends Annotation> T getAnnotation(@NotNull @IsPublicInstanceMethod Method method, @NotNull Class<T> annotationClass) {
    T annotation = method.getAnnotation(annotationClass);

    if (annotation == null) {
      Method bridgeMethod = getBridgeMethod(method);
      if (bridgeMethod == null) {
        return getMethodHierarchy(method).stream()
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

  public void buildMethodHierarchy(@NotNull(message = "No class specified") Class<?> clazz,
                                   @NotNull(message = "No method specified") @IsPublicInstanceMethod Method method,
                                   @NotNull(message = "Please supply a not null parent method list to be filled") List<Method> methodHierarchy) {
    String methodSignature = method.getName() + "(" + Stream.of(method.getParameterTypes()).map(Class::getCanonicalName).collect(Collectors.joining(",")) + ")";

    try {
        logger.info("Retrieving method: {}.{} ....", clazz.getCanonicalName(), methodSignature);
        method = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
        logger.info("Retrieving method: {}.{} .... found", clazz.getCanonicalName(), methodSignature);
        methodHierarchy.add(method);
      } catch (NoSuchMethodException e) {
        logger.info("Method not found: {}.{} ... not found.", clazz.getCanonicalName(), methodSignature);
      } finally {
        clazz = clazz.getSuperclass();
      }

    if (clazz != null) {
      sibling.buildMethodHierarchy(clazz, method, methodHierarchy);
    }
  }

  /** Returns a list of methods declared in the specified methods superclasses with the same signature as the specified method. */
  public List<Method> getMethodHierarchy(@NotNull(message = "No method specified") Method method) {
    Class<?> clazz = method.getDeclaringClass();
    List<Method> methodHierarchy = new ArrayList<>();

    buildMethodHierarchy(clazz, method, methodHierarchy);

    // Remove the first element since only parent methods should be returned.
    methodHierarchy.remove(0);

    return methodHierarchy;
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

}

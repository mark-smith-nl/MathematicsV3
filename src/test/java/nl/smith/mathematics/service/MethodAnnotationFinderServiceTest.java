package nl.smith.mathematics.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sun.org.apache.xpath.internal.Arg;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.service.MethodAnnotationFinderService;
import nl.smith.mathematics.util.RationalNumberUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MethodAnnotationFinderServiceTest {

  // SUT
  private final MethodAnnotationFinderService methodAnnotationFinderService;

  @Autowired
  public MethodAnnotationFinderServiceTest(
      MethodAnnotationFinderService methodAnnotationFinderService) {
    this.methodAnnotationFinderService = methodAnnotationFinderService;
  }

  @DisplayName("Testing null argument combinations for getting a method's annotation")
  @ParameterizedTest
  @MethodSource("methodAnnotationCombinationsWithNullValues")
   <T extends Annotation> void getAnnotation_nullArguments(Method method, Class<T> annotationClass) {
    assertThrows(ConstraintViolationException.class, () -> methodAnnotationFinderService.getAnnotation(method, annotationClass));
  }

  @DisplayName("Retrieve annotation directly from method")
  @Test
  void getAnnotation() throws NoSuchMethodException {
    MathematicalFunction annotation = methodAnnotationFinderService
        .getAnnotation(AbstractSubTestImpl.class.getDeclaredMethod("doIt", new Class<?>[0]),
            MathematicalFunction.class);

    assertNotNull(annotation);
    assertEquals("Annotation on doIt() in class AbstractSubTestImpl", annotation.description());
  }

  @DisplayName("Retrieve annotation indirectly from parent method")
  @Test
  void getAnnotation_fromParentMethod() throws NoSuchMethodException {
    MathematicalFunction annotation = methodAnnotationFinderService
        .getAnnotation(AbstractSubTestImpl.class.getDeclaredMethod("doItFromAnnotatedNonAbstractSuperClass", new Class<?>[0]),
            MathematicalFunction.class);

    assertNotNull(annotation);
    assertEquals("Annotation on doItFromAnnotatedNonAbstractSuperClass() in class AbstractTestImpl", annotation.description());
  }

  @DisplayName("Testing null argument in trying to retrieve a method's parent methods")
  @ParameterizedTest
  @NullSource
  void getParentMethods_nullArgument(Method method) {
    Exception exception = assertThrows(ConstraintViolationException.class, () -> methodAnnotationFinderService.getParentMethods(method));
    Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();
    assertEquals(1, constraintViolations.size());
    ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
    assertEquals("No method specified", constraintViolation.getMessage());
  }

  @DisplayName("Testing retrieval of a private or static method's parent methods")
  @ParameterizedTest
  @MethodSource("nonePublicInstanceMethods")
  void getParentMethods_using_none_publicInstanceMethods(Method method) {
    Exception exception = assertThrows(ConstraintViolationException.class, () -> methodAnnotationFinderService.getParentMethods(method));
    Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();
    assertEquals(1, constraintViolations.size());
    ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
    assertEquals(String.format("Method %s.%s(...) is not a public instance method", method.getDeclaringClass().getCanonicalName(), method.getName()), constraintViolation.getMessage());
  }

  @Test
  void getParentMethods_noParentMethods() throws NoSuchMethodException {
    Method method = AbstractTestImpl.class.getMethod("concreteMethod", new Class[]{int.class});

    assertEquals(0, methodAnnotationFinderService.getParentMethods(method).size());
  }

  @Test
  void getParentMethods_toString() throws NoSuchMethodException {
    Method method = AbstractTestImpl.class.getMethod("toString", new Class[]{});

    List<Method> parentMethods = methodAnnotationFinderService.getParentMethods(method);

    assertEquals(2, parentMethods.size());
    assertEquals(AbstractTest.class, parentMethods.get(0).getDeclaringClass());
    assertEquals(Object.class, parentMethods.get(1).getDeclaringClass());
  }

  @Test
  void getParentMethods_abstractGenericMethod() throws NoSuchMethodException {
    //Method method = AbstractTestImpl.class.getMethod("abstractGenericMethod", new Class[]{int.class});

    //List<Entry<Class, Method>> parentMethods = methodAnnotationFinderService.getParentMethods(method);

    //assertEquals(20, parentMethods.size());
  }

  private static Stream<Arguments> methodAnnotationCombinationsWithNullValues() {
    try {
      return Stream.of(
          Arguments.of(null, null),
          Arguments.of(Object.class.getDeclaredMethod("toString", new Class[0]), null),
          Arguments.of(null, NotNull.class));
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }

  private static Stream<Arguments> nonePublicInstanceMethods() {
    try {
      return Stream.of(
          Arguments.of(AbstractTestImpl.class.getDeclaredMethod("privateConcreteMethod", new Class<?>[0])),
          Arguments.of(AbstractTestImpl.class.getDeclaredMethod("publicStaticConcreteMethod", new Class<?>[0])));
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }

  public static abstract class AbstractTest <T> {

    public int concreteMethod(int i) {
      return i;
    }

    public abstract int abstractMethod(int i);

    public abstract T abstractGenericMethod(T arg);

    @MathematicalFunction(description = "Annotation on toString() in class AbstractTest")
    @Override
    public String toString() {
      return "AbstractTest{}";
    }

  }

  public static class AbstractTestImpl extends AbstractTest<String> {

    @Override
    public int abstractMethod(int i) {
      return 0;
    }

    @Override
    public String abstractGenericMethod(String arg) {
      return arg;
    }

    @Override
    public String toString() {
      return "AbstractTestImpl{}";
    }

    private void privateConcreteMethod() {}

    public static void publicStaticConcreteMethod() {}

    @MathematicalFunction(description = "Annotation on doItFromAnnotatedNonAbstractSuperClass() in class AbstractTestImpl")
    public void doItFromAnnotatedNonAbstractSuperClass() {}

  }

  public static class AbstractSubTestImpl extends AbstractTestImpl {

    @Override
    public String toString() {
      return "AbstractSubTestImpl{}";
    }

    @MathematicalFunction(description = "Annotation on doIt() in class AbstractSubTestImpl")
    public void doIt() {}

    @Override
    public void doItFromAnnotatedNonAbstractSuperClass() {
      super.doItFromAnnotatedNonAbstractSuperClass();
    }

  }
}
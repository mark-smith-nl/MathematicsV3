package nl.smith.mathematics.service;

import nl.smith.mathematics.annotation.MathematicalFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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

  @DisplayName("Retrieve annotation indirectly from a method's hierarchy")
  @Test
  void getAnnotation_fromMethodHierarchy() throws NoSuchMethodException {
    MathematicalFunction annotation = methodAnnotationFinderService
            .getAnnotation(AbstractSubTestImpl.class.getDeclaredMethod("doItFromAnnotatedNonAbstractSuperClass", new Class<?>[0]),
                    MathematicalFunction.class);

    assertNotNull(annotation);
    assertEquals("Annotation on doItFromAnnotatedNonAbstractSuperClass() in class AbstractTestImpl", annotation.description());
  }

  @DisplayName("Testing null argument (no method specified) in trying to retrieve a method's hierarchy")
  @ParameterizedTest
  @NullSource
  void getMethodHierarchy_nullArgument(Method method) {
    Exception exception = assertThrows(ConstraintViolationException.class, () -> methodAnnotationFinderService.getMethodHierarchy(method));
    Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();
    assertEquals(1, constraintViolations.size());
    ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
    assertEquals("No method specified", constraintViolation.getMessage());
  }

  @DisplayName("Testing retrieval a method's hierarchy using a private or static method")
  @ParameterizedTest
  @MethodSource("nonePublicInstanceMethods")
  void getMethodHierarchy_using_none_publicInstanceMethods(Method method) {
    Exception exception = assertThrows(ConstraintViolationException.class, () -> methodAnnotationFinderService.getMethodHierarchy(method));
    Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();
    assertEquals(1, constraintViolations.size());
    ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
    assertEquals(String.format("Method %s.%s(...) is not a public instance method", method.getDeclaringClass().getCanonicalName(), method.getName()), constraintViolation.getMessage());
  }

  @DisplayName("Testing retrieval a method's hierarchy using a public instance method - no parents")
  @Test
  void getMethodHierarchy_noParentMethods() throws NoSuchMethodException {
    Method method = AbstractTestImpl.class.getMethod("concreteMethod", new Class[]{int.class});

    assertEquals(0, methodAnnotationFinderService.getMethodHierarchy(method).size());
  }

  @DisplayName("Testing retrieval of AbstractTestImpl.toString method's hierarchy - multiple parents")
  @Test
  void getMethodHierarchy_toString() throws NoSuchMethodException {
    Method method = AbstractTestImpl.class.getMethod("toString", new Class[]{});

    List<Method> parentMethods = methodAnnotationFinderService.getMethodHierarchy(method);

    assertEquals(2, parentMethods.size());
    assertEquals(AbstractTest.class, parentMethods.get(0).getDeclaringClass());
    assertEquals(Object.class, parentMethods.get(1).getDeclaringClass());
  }

  @DisplayName("Testing private method in superclass illegally overridden as public in subclasss")
  @Test
  public void getParentmethods_privatemethodOverriddenasPublic() throws NoSuchMethodException {
    Class<?> clazz = AbstractSubTestImpl.class;
    Method method = clazz.getDeclaredMethod("overRiddenPrivateMethod", new Class<?>[]{int.class});
    Exception exception = assertThrows(ConstraintViolationException.class, () -> methodAnnotationFinderService.getMethodHierarchy(method));

    Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();
    assertEquals(1, constraintViolations.size());
    ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
    assertEquals(String.format("Method %s.%s(...) is not a public instance method", AbstractTest.class.getCanonicalName(), method.getName()), constraintViolation.getMessage());

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

    /** Method with same signature is declared as public in AbstractSubTestImpl. */
    private int overRiddenPrivateMethod(int i) {
      return i;
    }

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

    /** Method with same signature is declared as private in AbstractTest. */
    public int overRiddenPrivateMethod(int i) {
      return i;
    }

  }
}
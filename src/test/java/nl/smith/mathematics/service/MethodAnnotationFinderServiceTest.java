package nl.smith.mathematics.service;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import nl.smith.mathematics.service.MethodAnnotationFinderService;
import nl.smith.mathematics.util.RationalNumberUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MethodAnnotationFinderServiceTest {
  private final MethodAnnotationFinderService methodAnnotationFinderService;

  @Autowired
  public MethodAnnotationFinderServiceTest(
      MethodAnnotationFinderService methodAnnotationFinderService) {
    this.methodAnnotationFinderService = methodAnnotationFinderService;
  }

  @ParameterizedTest
  @DisplayName("Testing null argument")
  @NullSource
  void getParentMethods_nullArgument(Method method) {
    Exception exception = assertThrows(ConstraintViolationException.class, () -> methodAnnotationFinderService.getParentMethods(method));
    Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();
    assertEquals(1, constraintViolations.size());
    ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
    assertEquals("No method specified", constraintViolation.getMessage());
  }

  @ParameterizedTest
  @DisplayName("Testing invalid methods")
  @MethodSource("invalidMethods")
  void getParentMethods_usingInvalidMethods(Method method) {
    Exception exception = assertThrows(ConstraintViolationException.class, () -> methodAnnotationFinderService.getParentMethods(method));
    Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();
    assertEquals(1, constraintViolations.size());
    ConstraintViolation<?> constraintViolation = constraintViolations.stream().findFirst().get();
    assertEquals("No method specified", constraintViolation.getMessage());
  }

  @Test
  void getParentMethods_noParentMethods() throws NoSuchMethodException {
    Method method = AbstractTestImpl.class.getMethod("concreteMethod", new Class[]{int.class});

    assertEquals(0, methodAnnotationFinderService.getParentMethods(method).size());
  }

  @Test
  void getParentMethods_toString() throws NoSuchMethodException {
    Method method = AbstractTestImpl.class.getMethod("toString", new Class[]{});

    List<Entry<Class, Method>> parentMethods = methodAnnotationFinderService.getParentMethods(method);

    assertEquals(2, parentMethods.size());
    assertEquals(AbstractTest.class, parentMethods.get(0).getKey());
    assertEquals(Object.class, parentMethods.get(1).getKey());
  }


  @Test
  void getParentMethods_abstractGenericMethod() throws NoSuchMethodException {
    Method method = AbstractTestImpl.class.getMethod("abstractGenericMethod", new Class[]{int.class});

    List<Entry<Class, Method>> parentMethods = methodAnnotationFinderService.getParentMethods(method);

    assertEquals(20, parentMethods.size());
  }

  public static abstract class AbstractTest <T> {
    public int concreteMethod(int i) {
      return i;
    }

    public abstract int abstractMethod(int i);

    public abstract T abstractGenericMethod(T arg);

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

    private void privateConcreteMethod() {

    }
  }
}
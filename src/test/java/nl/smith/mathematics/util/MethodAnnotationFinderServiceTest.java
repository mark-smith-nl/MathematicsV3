package nl.smith.mathematics.util;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
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

  @Test
  void getParentMethods_noParentMethods() throws NoSuchMethodException {
    Method method = AbstractTestImpl.class.getMethod("concreteMethod", new Class[]{int.class});

    List<Method> parentMethods = methodAnnotationFinderService.getParentMethods(method);

    assertEquals(0, parentMethods.size());
  }

  @Test
  void getParentMethods_toString() throws NoSuchMethodException {
    Method method = AbstractTestImpl.class.getMethod("toString", new Class[]{});

    List<Method> parentMethods = methodAnnotationFinderService.getParentMethods(method);

    assertEquals(2, parentMethods.size());
  }

  @Test
  void getParentMethods_abstractGenericMethod() throws NoSuchMethodException {
    Method method = AbstractTestImpl.class.getMethod("abstractGenericMethod", new Class[]{int.class});

    List<Method> parentMethods = methodAnnotationFinderService.getParentMethods(method);

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
  }
}
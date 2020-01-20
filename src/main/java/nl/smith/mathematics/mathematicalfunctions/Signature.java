package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;

import java.lang.reflect.Method;

public class Signature {

  private final MathematicalFunction annotation;

  private final String name;

  private final Method method;

  private final int argumentCount;

  public Signature(MathematicalFunction annotation, String name, Method method, int argumentCount) {
    System.out.println(annotation.description());
    this.annotation = annotation;
    this.name = name;
    this.method = method;
    this.argumentCount = argumentCount;
  }

  public MathematicalFunction getAnnotation() {
    return annotation;
  }

  public int getArgumentCount() {
    return argumentCount;
  }

  public Method getMethod() {
    return method;
  }

  public String getName() {
    return name;
  }
}

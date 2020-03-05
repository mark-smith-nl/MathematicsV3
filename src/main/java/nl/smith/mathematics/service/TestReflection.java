package nl.smith.mathematics.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestReflection {

  private final int test;

  public TestReflection(int test) {
    this.test = test;
  }



  private static void doIt(int... i) {
    System.out.println("i");
  }

  /*private static void doIt(int i, int j) {
    System.out.println("i");
  }*/

  public static void main(String[] args) {
    Object o = new String("Test");

    doIt(1,2);

  }
}

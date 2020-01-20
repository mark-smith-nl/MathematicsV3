package nl.smith.mathematics.development;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Arrays;

public abstract class ParameterCount {

  public abstract void doIt();
  public abstract void doItOne(BigDecimal arg);
  public abstract void doItTwo(BigDecimal arg1, BigDecimal arg2);
  public abstract void doItThree(BigDecimal arg1, BigDecimal arg2, BigDecimal arg3);
  public abstract void doItMany(BigDecimal ... arg);
  public abstract void doItMany(BigDecimal arg, BigDecimal ... arg2);
  public static void main(String[] args) {
    Class<ParameterCount> parameterCountClass = ParameterCount.class;

    // @format:off
    Arrays.stream(parameterCountClass.getDeclaredMethods())
      .filter(m -> !Modifier.isStatic(m.getModifiers()))
      .filter(m -> Modifier.isAbstract(m.getModifiers()))
      .filter(m -> m.getParameterCount() > 0)
      .forEach(m -> System.out.println(String.format("Name %s, Argument count %d. Types %s. Array: %b. Return type: %s",
        m.getName(),
        m.getParameterCount(),
        m.getParameterTypes()[0].getName(),
        m.getParameterTypes()[0].isArray(),
        m.getReturnType())));
    // @format:off

    ParameterCount parameterCount = new ParameterCount(){
      @Override
      public void doIt() {

      }

      @Override
      public void doItOne(BigDecimal arg) {

      }

      @Override
      public void doItTwo(BigDecimal arg1, BigDecimal arg2) {

      }

      @Override
      public void doItThree(BigDecimal arg1, BigDecimal arg2, BigDecimal arg3) {

      }

      @Override
      public void doItMany(BigDecimal... arg) {

      }

      @Override
      public void doItMany(BigDecimal arg, BigDecimal... arg2) {

      }
    };
    parameterCount.doItMany(BigDecimal.ONE, new BigDecimal[] {BigDecimal.ONE, BigDecimal.ONE});
  }
}

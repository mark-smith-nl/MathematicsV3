package nl.smith.mathematics.mathematicalfunctions.implementation;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.definition.AbstractTestFunctionContainer;
import nl.smith.mathematics.service.MethodSignature;
import org.springframework.core.BridgeMethodResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BigDecimalTestFunctionContainer extends AbstractTestFunctionContainer<BigDecimal> {

    @Override
    public BigDecimal four(BigDecimal numberrrrr) {
        return numberrrrr;
    }

    @MathematicalFunction(description = "functionOne")
    public BigDecimal five(BigDecimal number) {
        return number;
    }

    @Override
    public BigDecimal six(BigDecimal number) {
        return number;
    }

    public static void main(String[] args) {

        Class<?> clazz = BigDecimalTestFunctionContainer.class;
        List<MethodSignature> methods = Stream.of(clazz.getMethods())
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .filter(m -> !Modifier.isAbstract(m.getModifiers()))
                .filter(m -> FunctionContainer.class.isAssignableFrom(m.getDeclaringClass()))
                .map(MethodSignature::getMethodSignature)
                .collect(Collectors.toList());

        methods.forEach(System.out::println);
    }

}

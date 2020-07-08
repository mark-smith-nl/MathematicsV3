package nl.smith.mathematics.service;

import nl.smith.mathematics.mathematicalfunctions.MathematicalFunctionMethodMapping;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;
import nl.smith.mathematics.util.ObjectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class MethodRunnerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MethodRunnerService.class);

    private final Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> recursiveFunctionContainers;

    private final Map<? extends Class<? extends Number>, List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>>> functionContainersByNumberType;

    private final Set<Class<? extends Number>> numberTypes;

    /**
     * The selected number type to work with
     */
    private Class<? extends Number> numberType;

    private final Map<Class<? extends Number>, Set<MathematicalFunctionMethodMapping>> mathematicalMethodsByNumberType = new HashMap<>();

    public MethodRunnerService(@NotEmpty Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> recursiveFunctionContainers) {
        LOGGER.info("Retrieved {} recursive function containers with duplicates.", recursiveFunctionContainers.size());
        removeDuplicateRecursiveFunctionContainers(recursiveFunctionContainers);
        LOGGER.info("Retrieved {} recursive function containers without duplicates.", recursiveFunctionContainers.size());

        this.recursiveFunctionContainers = Collections.unmodifiableSet(recursiveFunctionContainers);
        this.recursiveFunctionContainers.forEach(c -> c.getMathematicalFunctionMethodMappings().forEach(mf -> LOGGER.info("{} signature:\n\t{}", c.getClass().getCanonicalName(), mf.getSignature())));

        functionContainersByNumberType = Collections.unmodifiableMap(recursiveFunctionContainers.stream()
                .collect(Collectors.groupingBy(c -> c.getNumberType())));

        numberTypes = Collections.unmodifiableSet(functionContainersByNumberType.keySet());

        numberType = numberTypes.size() == 1 ? new ArrayList<Class<? extends Number>>(numberTypes).get(0) : null;

        LOGGER.info("\nSpecified number types: {}\nUsed number type: {}\n", numberTypes.stream().map(c -> c.getSimpleName()).sorted().collect(Collectors.joining(", ")), numberType == null ? "Number type not defined" : numberType.getSimpleName());

        buildMathematicalMethodsByNumberType();
    }

    // Since all function containers are recursive and thus have siblings, duplicate containers have to be removed.
    private void removeDuplicateRecursiveFunctionContainers(Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> recursiveFunctionContainers) {
        Map<Class<? extends RecursiveFunctionContainer>, RecursiveFunctionContainer> duplicateRecursiveFunctionContainersClasses = new HashMap<>();
        recursiveFunctionContainers.forEach(r -> {
            Class<? extends RecursiveFunctionContainer> clazz = (Class<RecursiveFunctionContainer>) r.getClass().getSuperclass();
            if (!duplicateRecursiveFunctionContainersClasses.containsKey(clazz)) {
                duplicateRecursiveFunctionContainersClasses.put(clazz, r);
            }
        });
        recursiveFunctionContainers.removeAll(duplicateRecursiveFunctionContainersClasses.values());
    }

    private void buildMathematicalMethodsByNumberType() {
        numberTypes.forEach(numberType -> {
            Set<MathematicalFunctionMethodMapping> mathematicalFunctionMethodMappings = new HashSet<>();
            List<MathematicalFunctionMethodMapping> duplicateMathematicalFunctionMethodMappings = new ArrayList<>();
            mathematicalMethodsByNumberType.put(numberType, mathematicalFunctionMethodMappings);

            List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> functionContainers = functionContainersByNumberType.get(numberType);

            functionContainers.forEach(c -> c.getMathematicalFunctionMethodMappings().forEach(mf -> {
                if (!mathematicalFunctionMethodMappings.add(mf)) {
                    duplicateMathematicalFunctionMethodMappings.add(mf);
                }
            }));

            if (duplicateMathematicalFunctionMethodMappings.isEmpty()) {
                return;
            }

            String error = duplicateMathematicalFunctionMethodMappings.stream()
                    .map(mf -> mf.getMethod().getDeclaringClass().getCanonicalName() + mf.getMethod().getName() + "--->" + mf.getSignature())
                    .collect(Collectors.joining("\n"));

            throw new IllegalStateException("Duplicate mathematical method references found in multiple classes.\n" + error);
        });
    }

    public Set<Class<? extends Number>> getNumberTypes() {
        return numberTypes;
    }

    public Class<? extends Number> getNumberType() {
        return numberType;
    }

    public void setNumberType(@NotEmpty(message = "Please specify a valid number type") String numberType) throws ClassNotFoundException {
        setNumberType((Class<? extends Number>) Class.forName(numberType));
    }

    public void setNumberType(Class<? extends Number> numberType) {
        this.numberType = numberType;
    }

    public List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> getAvailableFunctionContainers() {
        return functionContainersByNumberType.get(numberType);
    }

    public Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> getRecursiveFunctionContainers() {
        return recursiveFunctionContainers;
    }

    public <N extends Number> N invokeMathematicalMethod(@NotEmpty String mathematicalMethodName, N... arguments) {
        MathematicalFunctionMethodMapping mathematicalFunctionMethodMapping = getMathematicalFunctionMethodMapping(functionContainersByNumberType.get(numberType), mathematicalMethodName, arguments.length);

        if (arguments[0].getClass() != numberType) {
            throw new IllegalStateException(String.format("Wrong type of number class.\nThe number type of the %s instance is set to %s while the arguments for the method invocation are of type %s.\nBoth types should be equal.",
                    this.getClass().getCanonicalName(), numberType.getCanonicalName(), arguments[0].getClass().getCanonicalName()));
        }
        Method method = mathematicalFunctionMethodMapping.getMethod();
        RecursiveFunctionContainer container = mathematicalFunctionMethodMapping.getContainer();
        Object[] invocationParameters = getInvocationParameters(mathematicalFunctionMethodMapping, arguments);
        try {
            // Note: We have to use the sibling container since this is the @Validated container.
            return (N) method.invoke(container.getSibling(), invocationParameters);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private <N extends Number> Object[] getInvocationParameters(MathematicalFunctionMethodMapping mathematicalFunctionMethodMapping, N... arguments) {
        Object[] parameters = new Object[mathematicalFunctionMethodMapping.getParameterCount()];

        if (mathematicalFunctionMethodMapping.isVararg()) {
            int numberOfExplicitlyDeclaredParameters = mathematicalFunctionMethodMapping.getParameterCount() - 1;

            for (int i = 0; i < numberOfExplicitlyDeclaredParameters; i++) {
                parameters[i] = arguments[i];
            }

            int numberOfVarArgParameters = arguments.length - numberOfExplicitlyDeclaredParameters;
            Object varArgParameters = Array.newInstance(numberType, numberOfVarArgParameters);
            for (int i = 0; i < arguments.length - numberOfExplicitlyDeclaredParameters; i++) {
                Array.set(varArgParameters, i, arguments[numberOfExplicitlyDeclaredParameters + i]);
            }

            parameters[mathematicalFunctionMethodMapping.getParameterCount() - 1] = varArgParameters;
        } else {
            parameters = arguments;
        }

        return parameters;

    }

    private MathematicalFunctionMethodMapping getMathematicalFunctionMethodMapping(List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> recursiveFunctionContainers, String mathematicalMethodName, int parameterCount) {
        String errorMessage = String.format("Can not find a method %s accepting %d argument(s) of type %s.", mathematicalMethodName, parameterCount, numberType.getCanonicalName());
        ObjectWrapper<MathematicalFunctionMethodMapping> wrapper = new ObjectWrapper.NotNullObjectWrapper<>(null, MathematicalFunctionMethodMapping.class, errorMessage);
        recursiveFunctionContainers.forEach(c -> {
            Optional<MathematicalFunctionMethodMapping> a = c.getMathematicalFunctionMethodMapping(mathematicalMethodName, parameterCount);
            c.getMathematicalFunctionMethodMapping(mathematicalMethodName, parameterCount).ifPresent(mf -> wrapper.setValue(mf));
        });

        return wrapper.getValue();

    }

    public static void main(String[] args) {
        System.out.println(String.format("Can not find a method %s accepting %d arguments of type %s.", "okama", 4, "Integer"));
    }
}


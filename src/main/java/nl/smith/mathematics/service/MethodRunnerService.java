package nl.smith.mathematics.service;

import nl.smith.mathematics.mathematicalfunctions.MathematicalFunctionMethodMapping;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;
import nl.smith.mathematics.util.ObjectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Validated
public class MethodRunnerService {

    private static final  Logger LOGGER = LoggerFactory.getLogger(MethodRunnerService.class);

    private final Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>> recursiveFunctionContainers;

    private final Map<? extends Class<? extends Number>, List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>>> functionContainersByNumberType;

    private final Set<Class<? extends Number>> numberTypes;

    /**
     * The selected number type to work with
     */
    private Class<? extends Number> numberType;

    private final Map<Class<? extends Number>, Set<MathematicalFunctionMethodMapping>> mathematicalMethodsByNumberType = new HashMap<>();

    public MethodRunnerService(@NotEmpty Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>> recursiveFunctionContainers) {
        LOGGER.info("Retrieved {} recursive function containers with duplicates.", recursiveFunctionContainers.size());
        removeDuplicateRecursiveFunctionContainers(recursiveFunctionContainers);
        LOGGER.info("Retrieved {} recursive function containers without duplicates.", recursiveFunctionContainers.size());

        this.recursiveFunctionContainers = Collections.unmodifiableSet(recursiveFunctionContainers);
        this.recursiveFunctionContainers.forEach(c -> c.getMathematicalFunctionMethodMappings().forEach(mf -> LOGGER.info("{} signature:\n\t{}", c.getClass().getCanonicalName(), mf.getSignature())));

        functionContainersByNumberType = Collections.unmodifiableMap(recursiveFunctionContainers.stream()
                .collect(Collectors.groupingBy(RecursiveFunctionContainer::getNumberType)));

        numberTypes = Collections.unmodifiableSet(functionContainersByNumberType.keySet());

        numberType = numberTypes.size() == 1 ? new ArrayList<>(numberTypes).get(0) : null;

        LOGGER.info("\nSpecified number types: {}\nUsed number type: {}\n", numberTypes.stream().map(Class::getSimpleName).sorted().collect(Collectors.joining(", ")), numberType == null ? "Number type not defined" : numberType.getSimpleName());

        buildMathematicalMethodsByNumberType();
    }

    // Since all function containers are recursive and thus have siblings, duplicate containers have to be removed.
    private void removeDuplicateRecursiveFunctionContainers(Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>> recursiveFunctionContainers) {
        Map<Class<? extends RecursiveFunctionContainer<?, ?>>, RecursiveFunctionContainer<?, ?>> duplicateRecursiveFunctionContainersClasses = new HashMap<>();
        recursiveFunctionContainers.forEach(r -> {
            Class<? extends RecursiveFunctionContainer<?, ?>> clazz = (Class<RecursiveFunctionContainer<?, ?>>) r.getClass().getSuperclass();
            if (!duplicateRecursiveFunctionContainersClasses.containsKey(clazz)) {
                duplicateRecursiveFunctionContainersClasses.put(clazz, r);
            }
        });
        recursiveFunctionContainers.removeAll(duplicateRecursiveFunctionContainersClasses.values());
    }

    private void buildMathematicalMethodsByNumberType() {
        numberTypes.forEach(t -> {
            Set<MathematicalFunctionMethodMapping> mathematicalFunctionMethodMappings = new HashSet<>();
            List<MathematicalFunctionMethodMapping> duplicateMathematicalFunctionMethodMappings = new ArrayList<>();
            mathematicalMethodsByNumberType.put(t, mathematicalFunctionMethodMappings);

            List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>> functionContainers = functionContainersByNumberType.get(t);

            functionContainers.forEach(c -> c.getMathematicalFunctionMethodMappings().forEach(mf -> {
                if (!mathematicalFunctionMethodMappings.add(mf)) {
                    duplicateMathematicalFunctionMethodMappings.add(mf);
                }
            }));

            if (duplicateMathematicalFunctionMethodMappings.isEmpty()) {
                return;
            }

            String error = duplicateMathematicalFunctionMethodMappings.stream()
                    .map(MathematicalFunctionMethodMapping::toString)
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
        setNumberType(Class.forName(numberType).asSubclass(Number.class));
    }

    public void setNumberType(Class<? extends Number> numberType) {
        this.numberType = numberType;
    }

    public List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>> getAvailableFunctionContainers() {
        return functionContainersByNumberType.get(numberType);
    }

    public Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>> getRecursiveFunctionContainers() {
        return recursiveFunctionContainers;
    }

    public <N extends Number> N invokeMathematicalMethod(@NotEmpty String mathematicalMethodName, N[] arguments) {
        MathematicalFunctionMethodMapping<N> mathematicalFunctionMethodMapping = getMathematicalFunctionMethodMapping(functionContainersByNumberType.get(numberType), mathematicalMethodName, arguments.length);

        if (arguments[0].getClass() != numberType) {
            throw new IllegalStateException(format("Wrong type of number class.%nThe number type of the %s instance is set to %s while the arguments for the method invocation are of type %s.%nBoth types should be equal.",
                    this.getClass().getCanonicalName(), numberType.getCanonicalName(), arguments[0].getClass().getCanonicalName()));
        }

        return mathematicalFunctionMethodMapping.invokeWithNumbers(arguments);
    }

    private MathematicalFunctionMethodMapping getMathematicalFunctionMethodMapping(List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>> recursiveFunctionContainers, String mathematicalMethodName, int parameterCount) {
        String errorMessage = format("Can not find a method %s accepting %d argument(s) of type %s.", mathematicalMethodName, parameterCount, numberType.getCanonicalName());
        ObjectWrapper<MathematicalFunctionMethodMapping> wrapper = new ObjectWrapper.NotNullObjectWrapper<>(null, MathematicalFunctionMethodMapping.class, errorMessage);
        recursiveFunctionContainers.forEach(c -> c.getMathematicalFunctionMethodMapping(mathematicalMethodName, parameterCount).ifPresent(wrapper::setValue));

        return wrapper.getValue();
    }

}


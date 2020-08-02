package nl.smith.mathematics.service;

import com.sun.source.util.Trees;
import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunction.Type;
import nl.smith.mathematics.mathematicalfunctions.MathematicalFunctionMethodMapping;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalArithmeticFunctions;
import nl.smith.mathematics.util.ObjectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Validated
public class MethodRunnerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodRunnerService.class);

    private final Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>> recursiveFunctionContainers;

    private final Map<? extends Class<? extends Number>, List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>>> functionContainersByNumberType;

    private final Set<Class<? extends Number>> numberTypes;

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

        LOGGER.info("\nSpecified number types: {}", numberTypes.stream().map(Class::getSimpleName).sorted().collect(Collectors.joining(", ")));

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

    private static Class<? extends Number> getNumberTypeFromStringString(@NotEmpty(message = "Please specify a valid number type") String numberTypeAsString) {
        try {
            return Class.forName(numberTypeAsString).asSubclass(Number.class);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>> getAvailableFunctionContainersForNumberType(Class<? extends Number> numberType) {
        return functionContainersByNumberType.get(numberType);
    }

    public Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>> getRecursiveFunctionContainers() {
        return recursiveFunctionContainers;
    }

    public <N extends Number> N invokeMathematicalMethodForNumberType(@NotNull(message = "Please specify a valid number type (class).") Class<? extends Number> numberType, @NotEmpty String mathematicalMethodName, N[] arguments) {
        MathematicalFunctionMethodMapping<N> mathematicalFunctionMethodMapping = getMathematicalFunctionMethodMapping(functionContainersByNumberType.get(numberType), mathematicalMethodName, arguments.length, numberType);

        if (arguments[0].getClass() != numberType) {
            throw new IllegalStateException(format("Wrong type of number class.%nThe number type of the %s instance is set to %s while the arguments for the method invocation are of type %s.%nBoth types should be equal.",
                    this.getClass().getCanonicalName(), numberType.getCanonicalName(), arguments[0].getClass().getCanonicalName()));
        }

        return mathematicalFunctionMethodMapping.invokeWithNumbers(arguments);
    }

    public Set<MathematicalFunctionMethodMapping> getMathematicalMethodsForNumberTypeAndMathematicalMethodTypes(Class<? extends Number> numberType, Set<Type> types) {
        return mathematicalMethodsByNumberType.get(numberType).stream().filter(mm -> types.contains(mm.getType())).collect(Collectors.toCollection(() -> new
                TreeSet<MathematicalFunctionMethodMapping>(Comparator.comparing(MathematicalFunctionMethodMapping::toString))));
    }

    public Set<MathematicalFunctionMethodMapping> getMathematicalFunctionsForNumberType(Class<? extends Number> numberType) {
        return getMathematicalMethodsForNumberTypeAndMathematicalMethodTypes(numberType, new HashSet<>(Arrays.asList(Type.FUNCTION)));
    }

    public Set<MathematicalFunctionMethodMapping> getUnaryArithmeticMethodsForNumberType(Class<? extends Number> numberType) {
        return getMathematicalMethodsForNumberTypeAndMathematicalMethodTypes(numberType, new HashSet<>(Arrays.asList(Type.UNARY_OPERATION)));
    }

    public Set<MathematicalFunctionMethodMapping> getBinaryArithmeticMethodsForNumberType(Class<? extends Number> numberType) {
        return getMathematicalMethodsForNumberTypeAndMathematicalMethodTypes(numberType, new HashSet<>(Arrays.asList(Type.BINARY_OPERATION, Type.BINARY_OPERATION_HIGH_PRIORITY)));
    }

    public Set<MathematicalFunctionMethodMapping> getArithmeticMethodsForNumberType(Class<? extends Number> numberType) {
        return getMathematicalMethodsForNumberTypeAndMathematicalMethodTypes(numberType, new HashSet<>(Arrays.asList(Type.UNARY_OPERATION, Type.BINARY_OPERATION, Type.BINARY_OPERATION_HIGH_PRIORITY)));
    }

    private MathematicalFunctionMethodMapping getMathematicalFunctionMethodMapping(List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer<?, ?>>>
                                                                                           recursiveFunctionContainers, String mathematicalMethodName, int parameterCount, Class<? extends
            Number> numberType) {
        String errorMessage = format("Can not find a method %s accepting %d argument(s) of type %s.", mathematicalMethodName, parameterCount, numberType.getCanonicalName());
        ObjectWrapper<MathematicalFunctionMethodMapping> wrapper = new ObjectWrapper.NotNullObjectWrapper<>(null, MathematicalFunctionMethodMapping.class, errorMessage);
        recursiveFunctionContainers.forEach(c -> c.getMathematicalFunctionMethodMapping(mathematicalMethodName, parameterCount).ifPresent(wrapper::setValue));

        return wrapper.getValue();
    }

}

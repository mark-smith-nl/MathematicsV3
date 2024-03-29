package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.annotation.constraint.mathematicalfunctionargument.IsLargerThan;
import nl.smith.mathematics.domain.MathematicalFunctionMethodMapping;
import nl.smith.mathematics.service.RecursiveValidatedService;
import org.apache.logging.log4j.util.Strings;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public abstract class RecursiveFunctionContainer<N extends Number, S extends RecursiveFunctionContainer<N, S>> extends RecursiveValidatedService<S> {

    /** The annotation that describes the container. */
    protected final MathematicalFunctionContainer annotation;

    private final Class<N> numberType;

    /** Set of callable mathematical functions. */
    private final Set<MathematicalFunctionMethodMapping<N>> mathematicalFunctionMethodMappings;

    public RecursiveFunctionContainer(Class<?> mathematicalFunctionContainerClass) {
        annotation = getAnnotationFromGenericSuperClass(mathematicalFunctionContainerClass);

        numberType = getNumberTypeFromBeanClass();

        mathematicalFunctionMethodMappings = getMathematicalFunctionMethodMappingsFromGenericSuperClass(mathematicalFunctionContainerClass);

        logger.info("Instantiated {}", this.getClass().getCanonicalName());
    }

    private MathematicalFunctionContainer getAnnotationFromGenericSuperClass(Class<?> mathematicalFunctionContainerClass) {
        if (mathematicalFunctionContainerClass == null) {
            throw new IllegalStateException(format("Please specify an abstract mathematical function container class to extract the %s annotation from.",
                    MathematicalFunctionContainer.class.getCanonicalName()));
        }

        if (mathematicalFunctionContainerClass.getSuperclass() != RecursiveFunctionContainer.class) {
            throw new IllegalStateException(format("Incorrect class hierarchy.%nSpecified class %s should directly extend %s.",
                    mathematicalFunctionContainerClass.getCanonicalName(),
                    RecursiveFunctionContainer.class.getCanonicalName()));
        }

        MathematicalFunctionContainer annotation = mathematicalFunctionContainerClass.getAnnotation(MathematicalFunctionContainer.class);
        if (annotation == null) {
            throw new IllegalStateException(format("Please annotate your class %s with %s.",
                    mathematicalFunctionContainerClass.getCanonicalName(),
                    MathematicalFunctionContainer.class.getCanonicalName()));
        }

        return annotation;
    }

    /**
     * Protected for test purposes.
     */
    private Class<N> getNumberTypeFromBeanClass() {
        Class<?> functionContainerClazz = this.getClass();

        Type genericInterfaceClazz = functionContainerClazz.getGenericSuperclass();

        return (Class<N>) ((ParameterizedType) genericInterfaceClazz).getActualTypeArguments()[0];
    }

    private Set<MathematicalFunctionMethodMapping<N>> getMathematicalFunctionMethodMappingsFromGenericSuperClass(Class<?> mathematicalFunctionContainerClass) {
        Map<String, List<MathematicalFunctionMethodMapping<N>>> mathematicalMethodsBySignature = Stream.of(mathematicalFunctionContainerClass.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(m -> Modifier.isAbstract(m.getModifiers()))
                .filter(m -> m.getAnnotation(MathematicalFunction.class) != null)
                .map(m -> new MathematicalFunctionMethodMapping<>(this, m))
                .collect(Collectors.groupingBy(MathematicalFunctionMethodMapping::getSignature));

        Map<String, List<MathematicalFunctionMethodMapping<N>>> duplicateMathematicalMethods = mathematicalMethodsBySignature.entrySet().stream().filter(e -> e.getValue().size() > 1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (duplicateMathematicalMethods.isEmpty()) {
            Set<MathematicalFunctionMethodMapping<N>> mathematicalFunctionMethodMappings = new HashSet<>();
            mathematicalMethodsBySignature.values().forEach(mathematicalFunctionMethodMappings::addAll);
            return mathematicalFunctionMethodMappings;
        }

        List<String> errors = new ArrayList<>();
        errors.add(format("%nMultiple colliding mathematical method references found in enclosing class %s.", mathematicalFunctionContainerClass.getCanonicalName()));
        errors.add(format("Please specify the correct mathematical function name in the annotation (%s.name) annotating your methods.", MathematicalFunction.class.getCanonicalName()));
        duplicateMathematicalMethods.forEach((s, values) -> {
            errors.add(format("The signature %s references the following Java methods:", s));
            values.forEach(mf -> errors.add(format("- %s (Description: %s)", mf.getMethodName(), mf.getDescription())));
        });

        throw new IllegalStateException(Strings.join(errors, '\n'));
    }

    public Class<N> getNumberType() {
        return numberType;
    }

    public String getName() {
        return annotation.name();
    }

    public String getDescription() {
        return annotation.description();
    }

    public Set<MathematicalFunctionMethodMapping<N>> getMathematicalFunctionMethodMappings() {
        return mathematicalFunctionMethodMappings;
    }

    public Optional<MathematicalFunctionMethodMapping<N>> getMathematicalFunctionMethodMapping(@NotEmpty String mathematicalFunctionName, @IsLargerThan("0") int parameterCount) {
        List<MathematicalFunctionMethodMapping<N>> mathematicalFunctionsWithIdenticalNames = mathematicalFunctionMethodMappings.stream().filter(m -> mathematicalFunctionName.equals(m.getName())).collect(Collectors.toList());

        MathematicalFunctionMethodMapping<N> mathematicalFunctionMethodMapping = mathematicalFunctionsWithIdenticalNames.stream().filter(mf -> !mf.isVararg()).filter(mf -> mf.getParameterCount() == parameterCount).findFirst().orElse(null);

        if (mathematicalFunctionMethodMapping == null) {
            mathematicalFunctionMethodMapping = mathematicalFunctionsWithIdenticalNames.stream().filter(MathematicalFunctionMethodMapping::isVararg).filter(mf -> mf.getParameterCount() <= parameterCount).findFirst().orElse(null);
        }

        return Optional.ofNullable(mathematicalFunctionMethodMapping);
    }
}

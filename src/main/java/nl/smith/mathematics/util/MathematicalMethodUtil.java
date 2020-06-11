package nl.smith.mathematics.util;

import nl.smith.mathematics.mathematicalfunctions.MathematicalFunctionMethodMapping;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;

import java.lang.reflect.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility to check if @{@link MathematicalFunctionMethodMapping} annotated methods use and return the correct value types.
 */
public class MathematicalMethodUtil {

    private MathematicalMethodUtil() {
        throw new IllegalStateException(String.format("Can not instantiate %s", this.getClass().getCanonicalName()));
    }

    public static void checkGenericsEnclosingClass(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalStateException("Please specify a class.");
        }

        boolean valid = false;

        if (clazz.getSuperclass() == RecursiveFunctionContainer.class) {
            TypeVariable<? extends Class<?>>[] typeParameters = clazz.getTypeParameters();
            // Note: There should be two typeparameters, each with one boundary.
            // The first parameter denotes the number type: it should extend number.
            // The second parameter is the concrete service class needed to bidirectional inject the services.
            if (typeParameters.length == 2) {
                Type[] bounds = typeParameters[0].getBounds();
                if (bounds.length == 1) {
                    valid = bounds[0] == Number.class;
                }
            }
        }

        if (!valid) {
            throw new IllegalStateException(String.format("Improper use of generics.\n" +
                            "Define your class as public abstract <N extends %s, S extends %s> extends %s<N, S>",
                    Number.class.getSimpleName(),
                    clazz.getSimpleName(),
                    RecursiveFunctionContainer.class.getSimpleName()));
        }
    }

    public static void checkModifiers(Method method) {
        if (method == null) {
            throw new IllegalStateException("Please specify a method.");
        }

        int modifier = method.getModifiers();

        if (!Modifier.isPublic(modifier) || !Modifier.isAbstract(modifier)) {
            throw new IllegalStateException(String.format("The method %s.%s should be public and abstract.",
                    method.getDeclaringClass().getCanonicalName(),
                    method.getName()));
        }
    }

    public static void checkReturnType(Method method) {
        if (method == null) {
            throw new IllegalStateException("Please specify a method.");
        }

        boolean valid = false;

        // There should not be any declared type parameters.
        if (method.getTypeParameters().length == 0) {
            Type genericReturnType = method.getGenericReturnType();
            // Only generic or geneic return types are allowed.
            boolean isGenericArray = GenericArrayType.class.isAssignableFrom(genericReturnType.getClass());
            boolean isTypeVariable = TypeVariable.class.isAssignableFrom(genericReturnType.getClass());

            if (isGenericArray || isTypeVariable) {
                if (isGenericArray) {
                    GenericArrayType genericArrayType = (GenericArrayType) genericReturnType;
                    TypeVariable typeVariable = (TypeVariable) genericArrayType.getGenericComponentType();

                    if (typeVariable.getBounds().length == 1) {
                        valid = typeVariable.getBounds()[0] == Number.class;
                    }
                }

                if (isTypeVariable) {
                    TypeVariable typeVariable = (TypeVariable) genericReturnType;

                    if (typeVariable.getBounds().length == 1) {
                        valid = typeVariable.getBounds()[0] == Number.class;
                    }
                }
            }
        }

        if (!valid) {
            throw new IllegalStateException(String.format("The return type of %s.%s is not valid.\n" +
                            "It should be: T or T[] with <T extends %s>",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    Number.class.getSimpleName()));
        }

    }

    /**
     * Only methods with the following signature are valid:
     */
    public static void checkArguments(Method method) {
        if (method == null) {
            throw new IllegalStateException("Please specify a method.");
        }

        boolean valid = false;
        // There should be at least one parameter.
        int parameterCount = method.getParameterCount();
        if (parameterCount > 0) {
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            valid = true;
            for (int i = 0; i < parameterCount - 1; i++) {
                Type genericParameterType = genericParameterTypes[i];
                valid = TypeVariable.class.isAssignableFrom(genericParameterType.getClass());
                if (!valid) {
                    break;
                }
            }

            if (valid) {
                Type genericParameterType = genericParameterTypes[parameterCount - 1];
                valid = GenericArrayType.class.isAssignableFrom(genericParameterType.getClass()) ||
                        TypeVariable.class.isAssignableFrom(genericParameterType.getClass());
            }
        }

        if (!valid) {
            throw new IllegalStateException(String.format("The argument types of %s.%s are not valid.\n" +
                            "It should be: T or T[] with <T extends %s>",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    Number.class.getSimpleName()));
        }
    }

    public static String getMathematicalMethodSignature(MathematicalFunctionMethodMapping mathematicalFunctionMethodMapping) {
        if (mathematicalFunctionMethodMapping == null) {
            throw new IllegalStateException("Please specify a mathematical method.");
        }

        return  mathematicalFunctionMethodMapping.getName() + "(" +
                getMathematicalMethodGenericParameterTypesAsString(mathematicalFunctionMethodMapping) + ")";
    }

    private static String getMathematicalMethodGenericReturnTypeAsString(MathematicalFunctionMethodMapping mathematicalFunctionMethodMapping) {
        Type genericReturnType = mathematicalFunctionMethodMapping.getMethod().getGenericReturnType();

        if (GenericArrayType.class.isAssignableFrom(genericReturnType.getClass())) {
            return ((GenericArrayType) genericReturnType).getTypeName();
        }

        return ((TypeVariable) genericReturnType).getName();

    }

    private static String getMathematicalMethodGenericParameterTypesAsString(MathematicalFunctionMethodMapping mathematicalFunctionMethodMapping) {
        Type[] genericParameterTypes = mathematicalFunctionMethodMapping.getMethod().getGenericParameterTypes();

        return Stream.of(mathematicalFunctionMethodMapping.getMethod().getGenericParameterTypes()).map(genericParameterType -> {
            if (GenericArrayType.class.isAssignableFrom(genericParameterType.getClass())) {
                return ((GenericArrayType) genericParameterType).getTypeName();
            }

            return ((TypeVariable) genericParameterType).getName();
        }).collect(Collectors.joining(", "));

    }
}
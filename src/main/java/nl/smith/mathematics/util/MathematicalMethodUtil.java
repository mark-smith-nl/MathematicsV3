package nl.smith.mathematics.util;

import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;

import java.lang.reflect.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Utility to check if @{@link nl.smith.mathematics.mathematicalfunctions.MathematicalMethod} annotated methods use and return the correct value types. */
public class MathematicalMethodUtil {

    private MathematicalMethodUtil() {
        throw new IllegalStateException(String.format("Can not instantiate %s", this.getClass().getCanonicalName()));
    }

    public static void checkGenericsEnclosingClass(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalStateException("Please specify a class.");
        }

        boolean validGenericClass = false;

        if (clazz.getSuperclass() == RecursiveFunctionContainer.class) {
            TypeVariable<? extends Class<?>>[] typeParameters = clazz.getTypeParameters();
            if (typeParameters.length == 2) {
                Type[] bounds = typeParameters[0].getBounds();
                if (bounds.length == 1) {
                    validGenericClass = bounds[0] == Number.class;
                }
            }
        }

        if (!validGenericClass) {
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

        int modifier  = method.getModifiers();

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

        boolean validReturnType = false;
        Type genericReturnType = method.getGenericReturnType();
        boolean isGenericArray = GenericArrayType.class.isAssignableFrom(genericReturnType.getClass());
        boolean isTypeVariable = TypeVariable.class.isAssignableFrom(genericReturnType.getClass());

        if (isGenericArray || isTypeVariable) {
           if (isGenericArray) {
               GenericArrayType genericArrayType = (GenericArrayType) genericReturnType;
               validReturnType = genericArrayType.getGenericComponentType() == Number.class;
           }

           if (isTypeVariable) {
               TypeVariable typeVariable = (TypeVariable) genericReturnType;
               if (typeVariable.getBounds().length == 1) {
                   validReturnType = typeVariable.getBounds()[0] == Number.class;
               }
           }
        }

        if (!validReturnType) {
            throw new IllegalStateException(String.format("The return type of %s%s is not valid.\n" +
                    "It should be: T or T[] with <T extends %s>",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    Number.class.getSimpleName()));
        }

    }



    public static String getSignatureFromMethod(Method method) {
        int modifiers = method.getModifiers();

        StringBuilder signature = new StringBuilder("");
        signature.append(Modifier.isPublic(modifiers) ? "public " : "");
        signature.append(Modifier.isProtected(modifiers) ? "protected " : "");
        signature.append(Modifier.isPrivate(modifiers) ? "private " : "");
        signature.append(Modifier.isAbstract(modifiers) ? "abstract " : "");
        signature.append(Modifier.isStatic(modifiers) ? "static " : "");
        signature.append(method.getReturnType().getSimpleName() + " ");
        signature.append(method.getDeclaringClass().getSimpleName() + "." + method.getName());
        signature.append("(" + Stream.of(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")");

        return signature.toString();
    }
}

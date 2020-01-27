package nl.smith.mathematics.development;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.stream.Stream;

public class GenericClass<Y, T, X extends Number & Comparable> {

    public T doIt(T arg1, Y arg2) {
        return null;
    }

    public static void main(String[] args) {
        System.out.println("Hello world 123456");
        Class<?> clazz = GenericClass.class;
        Type genericSuperclass = clazz.getGenericSuperclass();
        System.out.println(clazz);
        System.out.println(genericSuperclass);
        Stream.of(clazz.getTypeParameters()).forEach(System.out::println);
        TypeVariable<? extends Class<?>> typeParameter = clazz.getTypeParameters()[2];
        Type[] bounds = typeParameter.getBounds();
        System.out.println();


    }
}

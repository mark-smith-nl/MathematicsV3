package nl.smith.mathematics.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/*
  @author mark
 * Annotation to mark a method as a mathematical function.
 * This annotation should be placed on abstract generic public methods.
 * The method should return the specified generic return type or a generic array of the specified generic return type.
 * A list of the specified generic return type is not accepted.
 * The method should accept at least one argument. All parameters should be of the specified generic return type.
 * Varargs are accepted as (the last) argument.
 */
public @interface MathematicalFunction {

	String name() default "";

	String description();
}

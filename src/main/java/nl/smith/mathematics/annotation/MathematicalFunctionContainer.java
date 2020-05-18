/**
 * 
 */
package nl.smith.mathematics.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * @author mark
 * Annotation to mark a class as containing {@link MathematicalFunction} annotated methods.
 */
public @interface MathematicalFunctionContainer {

	String name() default "";

	String description();
}

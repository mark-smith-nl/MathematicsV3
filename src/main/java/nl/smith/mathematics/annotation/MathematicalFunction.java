/**
 * 
 */
package nl.smith.mathematics.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * @author mark
 * Mark a class that it contains mathematical functions.
 */
public @interface MathematicalFunction {

}

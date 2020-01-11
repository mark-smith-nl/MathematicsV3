/**
 * 
 */
package nl.smith.mathematics.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Retention(CLASS)
@Target(TYPE)
@Component
/**
 * @author mark
 * Mark a class that it contains mathematical functions.
 */
public @interface FunctionContainer {

}

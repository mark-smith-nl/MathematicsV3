package nl.smith.mathematics.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;

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

	Type type() default Type.FUNCTION;

	enum Type {
		FUNCTION("[a-zA-Z]{2,}", parameterCount -> parameterCount > 0),
		UNARY_OPERATION("[\\-+]", parameterCount -> parameterCount == 1),
		BINARY_OPERATION("[\\-+\\*/]", parameterCount -> parameterCount == 2),
		BINARY_OPERATION_HIGH_PRIORITY("[\\-+\\*/]", parameterCount -> parameterCount == 2),;

		private final String regex;

		private final Predicate<Integer> parameterCountChecker;

		Type(String regex, Predicate<Integer> parameterCountChecker) {
			this.regex = regex;
			this.parameterCountChecker = parameterCountChecker;
		}

		public String getRegex() {
			return regex;
		}

		public Predicate<Integer> getParameterCountChecker() {
			return parameterCountChecker;
		}
	}

}

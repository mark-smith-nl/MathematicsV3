package nl.smith.mathematics;

import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;
import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalGoniometricFunctions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MathematicsV3ApplicationTests {

	private final Set<FunctionContainer> functionContainers;

	@Autowired
	public MathematicsV3ApplicationTests(Set<FunctionContainer> functionContainers) {
		this.functionContainers = functionContainers;
	}

	@Test
	void contextLoads() {
		assertTrue(functionContainers.size() > 0);
	}

	public static void main(String[] args) {
		Class clazz = BigDecimalGoniometricFunctions.class;
		Stream.of(clazz.getDeclaredMethods()).forEach(m -> {
			System.out.println(String.format(
					"Name %s. Annotations %d. Declared annotations %d. Bridge %s",
					m.getName(),
					m.getAnnotations().length,
					m.getDeclaredAnnotations().length,
					m.isBridge()));
			Class<?> superclass = m.getDeclaringClass().getSuperclass();
			try {
				Method declaredMethod = superclass.getDeclaredMethod(m.getName(), m.getParameterTypes());
				System.out.println(declaredMethod.getAnnotations().length);
			} catch (NoSuchMethodException e) {
				//e.printStackTrace();
			}

		});
	}

}
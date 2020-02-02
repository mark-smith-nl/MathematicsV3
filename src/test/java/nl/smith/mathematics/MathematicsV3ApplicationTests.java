package nl.smith.mathematics;

import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalGoniometricFunctions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;
import java.util.stream.Stream;

@SpringBootTest
class MathematicsV3ApplicationTests {

	@Test
	void contextLoads() {
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
package nl.smith.mathematics.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.functions.bigdecimal.BigDecimalStatisticalFunctions;
import nl.smith.mathematics.functions.definition.Functions;

@Service
public class MethodRunnerService {

	private final static Logger LOGGER = LoggerFactory.getLogger(MethodRunnerService.class);

	private final Set<Functions<? extends Number>> functionContainers;

	/** Functions mapped by number class they return and use. */
	private final Map<Class<? extends Number>, Set<Functions<? extends Number>>> numberTypeClassFunctionContainers = new HashMap<>();

	/** Methods mapped by functionContainer. */
	private final Map<Functions<? extends Number>, Set<Method>> functionContainer = new HashMap<>();

	@Autowired
	public MethodRunnerService(Set<Functions<? extends Number>> functionContainers) {
		this.functionContainers = functionContainers;

		// Add function container to the appropriate number type function container
		functionContainers.forEach(functionContainer -> {
			Class<? extends Functions> functionContainerClazz = functionContainer.getClass();
			LOGGER.info("Process function container: {}: ", functionContainerClazz.getCanonicalName());

			ParameterizedType parameterizedType = (ParameterizedType) functionContainerClazz.getGenericInterfaces()[0];
			Class<? extends Number> numberTypeClass = (Class<? extends Number>) parameterizedType.getActualTypeArguments()[0];

			Set<Functions<? extends Number>> set = numberTypeClassFunctionContainers.get(numberTypeClass);
			if (set == null) {
				LOGGER.info("Creating container for functions using {}.", numberTypeClass.getCanonicalName());
				set = new HashSet<>();
				numberTypeClassFunctionContainers.put(numberTypeClass, set);
			}

			set.add(functionContainer);
			Set<Method> methods = Arrays.asList(functionContainerClazz.getDeclaredMethods()).stream().filter(method -> !method.isBridge()).collect(Collectors.toSet());
			methods.forEach(method -> {
				if (method.getName().equals("deviation")) {

					System.out.println("--->" + method);
					if (method.getReturnType().equals(Number.class)) {
						System.out.print("Number ");
						System.out.println(method.getAnnotations().length);
					}
					if (method.getReturnType().equals(BigDecimal.class)) {
						System.out.print("BigDecimal ");
						System.out.println(method.getAnnotations().length);
					}
				}
			});
			System.out.println("-------");

		});

		LOGGER.info("Number of function containers: {}", functionContainers.size());

	}

	public static void main(String[] args) {
		BigDecimalStatisticalFunctions functionContainer = new BigDecimalStatisticalFunctions();
		Class clazz = functionContainer.getClass();
		Class superClass = clazz.getSuperclass();
		List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
		System.out.println(methods.size());
		methods.forEach(method -> {
			System.out.println(String.format("public %s %s.%s() isBridge: %s annotations: %d", method.getReturnType().getSimpleName(), clazz.getSimpleName(), method.getName(),
					method.isBridge(), method.getAnnotations().length));
			System.out.println(method.getParameters());
			Type parameterizedType = method.getParameters()[0].getParameterizedType();
			Class parameterClass = (Class) parameterizedType;

			if (method.isBridge()) {
				try {
					Method annotatedMethod = superClass.getDeclaredMethod(method.getName(), parameterClass);
					System.out.println(annotatedMethod.getAnnotations().length);
					MathematicalFunction annotation = (MathematicalFunction) annotatedMethod.getAnnotations()[0];

					System.out.println(annotation.description());
				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			} else {
				try {
					System.out.println(method.isBridge());
					System.out.println(method.invoke(functionContainer, BigDecimal.ONE));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
}

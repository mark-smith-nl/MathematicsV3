package nl.smith.mathematics.service;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

		// Add function container to the appropriate typed function container
		functionContainers.forEach(functionContainer -> {
			Class<? extends Functions> functionContainerClazz = functionContainer.getClass();

			ParameterizedType parameterizedType = (ParameterizedType) functionContainerClazz.getGenericInterfaces()[0];
			Class<? extends Number> numberTypeClass = (Class<? extends Number>) parameterizedType.getActualTypeArguments()[0];

			LOGGER.info("Process function container: {}: ", functionContainerClazz.getCanonicalName());

			Set<Functions<? extends Number>> set = numberTypeClassFunctionContainers.get(numberTypeClass);
			if (set == null) {
				LOGGER.info("Creating container for functions using {}.", numberTypeClass.getCanonicalName());
				set = new HashSet<>();
				numberTypeClassFunctionContainers.put(numberTypeClass, set);
			}

			set.add(functionContainer);

			Arrays.asList(functionContainerClazz.getDeclaredMethods()).forEach(System.out::println);

		});

		LOGGER.info("Number of function containers: {}", functionContainers.size());

	}

}

package nl.smith.mathematics.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.smith.mathematics.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.functions.definition.FunctionContainer;

@Service
public class MethodRunnerService {

	private final static Logger LOGGER = LoggerFactory.getLogger(MethodRunnerService.class);

	private final Map<Class<?>, String> functions = new HashMap<>();

	private final Set<Class<? extends Number>> numberTypes = new HashSet<>();

	private String descriptions;

	@Autowired
	public MethodRunnerService(Set<FunctionContainer<? extends Number>> functionContainers) {
		initialize(functionContainers);
	}

	private void initialize(Set<FunctionContainer<? extends Number>> functionContainers) {
		inspectNumberTypeContainers(functionContainers);
	}

	/** Add function container to the appropriate set of number type function containers. */
	private void inspectNumberTypeContainers(Set<FunctionContainer<? extends Number>> functionContainers) {
		final StringBuilder descriptions = new StringBuilder();
		functionContainers.forEach(functionContainer -> {
			Class<?> proxyFunctionContainerClazz = functionContainer.getClass();
			Class<?> functionContainerClazz = proxyFunctionContainerClazz.getSuperclass();
			LOGGER.info("Inspect function container: {}: ", functionContainerClazz.getCanonicalName());

			Type genericInterfaceClazz = functionContainerClazz.getGenericSuperclass();
			Class<?> functionContainerSuperClazz = functionContainerClazz.getSuperclass();

			if (functionContainerSuperClazz.isAnnotationPresent(MathematicalFunctionContainer.class)) {
				@SuppressWarnings("unchecked")
				Class<? extends Number> numberType = (Class<? extends Number>) ((ParameterizedType) genericInterfaceClazz).getActualTypeArguments()[0];

				numberTypes.add(numberType);

				if (!functions.containsKey(functionContainerSuperClazz)) {
					MathematicalFunctionContainer annotation = functionContainerSuperClazz.getAnnotation(MathematicalFunctionContainer.class);
					String description = annotation.description();
					descriptions.append(description).append("\n\n");
					functions.put(functionContainerSuperClazz, annotation.description());
				}
			} else {
				LOGGER.warn("The generic interface {} should be annotated with {}.", functionContainerSuperClazz.getCanonicalName(),
						MathematicalFunctionContainer.class.getCanonicalName());
			}
		});

		this.descriptions = descriptions.toString();

		LOGGER.info("\n\nDetected numberr types: {}", getNumberTypesAsString());

		LOGGER.info("\n\nFunctions:\n{}", getDescriptions());

	}

	public Set<Class<? extends Number>> getNumberTypes() {
		return numberTypes;
	}

	public String getNumberTypesAsString() {
		// @formatter:off
		return numberTypes.stream()
				.map(clazz -> clazz.getCanonicalName())
				.collect(Collectors.joining(", "));
		// @formatter:on
	}

	public String getDescriptions() {
		return descriptions;
	}

}

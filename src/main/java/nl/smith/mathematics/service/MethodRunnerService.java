package nl.smith.mathematics.service;

import nl.smith.mathematics.mathematicalfunctions.MathematicalMethod;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class MethodRunnerService {

  private final static Logger LOGGER = LoggerFactory.getLogger(MethodRunnerService.class);

  private final Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> recursiveFunctionContainers;

  private final Map<? extends Class<? extends Number>, List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>>> functionContainersByNumberType;

  private final Set<Class<? extends Number>> numberTypes;

  /** The selected number type to work with */
  private Class<? extends Number> numberType;

  private final Map<Class<? extends Number>, Map<String, MathematicalMethod>> mathematicalMethodsByNumberTypeAndSignature = new HashMap<>();

  public MethodRunnerService(@NotEmpty Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> recursiveFunctionContainers) {
    LOGGER.info("Retrieved {} recursive function containers with duplicates.", recursiveFunctionContainers.size());
    removeDuplicateRecursiveFunctionContainers(recursiveFunctionContainers);
    LOGGER.info("Retrieved {} recursive function containers without duplicates.", recursiveFunctionContainers.size());

    this.recursiveFunctionContainers = Collections.unmodifiableSet(recursiveFunctionContainers);

    functionContainersByNumberType = Collections.unmodifiableMap(recursiveFunctionContainers.stream()
      .collect(Collectors.groupingBy(container -> container.getNumberType())));

    numberTypes = Collections.unmodifiableSet(functionContainersByNumberType.keySet());

    numberType = numberTypes.size() == 1 ? new ArrayList<Class<? extends Number>>(numberTypes).get(0) :  null;

    LOGGER.info("\nSpecified number types: {}\nUsed number type: {}\n", numberTypes.stream().map(c -> c.getSimpleName()).sorted().collect(Collectors.joining(", ")), numberType == null ? "Number type not defined": numberType.getSimpleName());

    buildMathematicalMethodsByNumberTypeAndSignature();

    recursiveFunctionContainers.forEach(f -> f.getMathematicalFunctions().forEach((k, v) -> LOGGER.info("\n{}\n", k)));
  }

  // Since all function containers are recursive and thus have siblings, duplicate containers have to be removed.
  private void removeDuplicateRecursiveFunctionContainers(Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> recursiveFunctionContainers) {
    Map<Class<? extends RecursiveFunctionContainer>, RecursiveFunctionContainer> duplicateRecursiveFunctionContainersClasses = new HashMap<>();
    recursiveFunctionContainers.forEach(r -> {
      Class<? extends RecursiveFunctionContainer> clazz = (Class<RecursiveFunctionContainer>) r.getClass().getSuperclass();
      if (!duplicateRecursiveFunctionContainersClasses.containsKey(clazz)) {
        duplicateRecursiveFunctionContainersClasses.put(clazz, r);
      }
    });
    recursiveFunctionContainers.removeAll(duplicateRecursiveFunctionContainersClasses.values());
  }

  private void buildMathematicalMethodsByNumberTypeAndSignature() {
    numberTypes.forEach(numberType -> {
      Map<String, MathematicalMethod> mathematicalMethodsMap = new HashMap<>();
      mathematicalMethodsByNumberTypeAndSignature.put(numberType, mathematicalMethodsMap);

      List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> functionContainers = functionContainersByNumberType.get(numberType);
      functionContainers.forEach(functionContainer -> mathematicalMethodsMap.putAll(functionContainer.getMathematicalFunctions()));
    });
  }

  public Set<Class<? extends Number>> getNumberTypes() {
    return numberTypes;
  }

  public Class<? extends Number> getNumberType() {
    return numberType;
  }

  public void setNumberType(@NotEmpty(message = "Please specify a valid number type") String numberType) throws ClassNotFoundException {
    setNumberType((Class<? extends Number>) Class.forName(numberType));
  }


  public void setNumberType(Class<? extends Number> numberType) {
    this.numberType = numberType;
  }

  public List<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> getAvailableFunctionContainers(){
    return functionContainersByNumberType.get(numberType);
  }

  public Set<RecursiveFunctionContainer<? extends Number, ? extends RecursiveFunctionContainer>> getRecursiveFunctionContainers() {
    return recursiveFunctionContainers;
  }

}


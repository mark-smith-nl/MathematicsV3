package nl.smith.mathematics.service;

import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service public class MethodRunnerService {

  private final static Logger LOGGER = LoggerFactory.getLogger(MethodRunnerService.class);

  private final Set<FunctionContainer<? extends Number>> functionContainers;

  private final Map<? extends Class<? extends Number>, List<FunctionContainer<? extends Number>>> functionContainersByNumberType;

  private final Set<Class<? extends Number>> numberTypes;

  /** The selected number type to work with */
  private Class<? extends Number> numberType;

  public MethodRunnerService(@NotEmpty Set<FunctionContainer<? extends Number>> functionContainers) {
    this.functionContainers = Collections.unmodifiableSet(functionContainers);

    functionContainersByNumberType = Collections.unmodifiableMap(functionContainers.stream()
      .collect(Collectors.groupingBy(container -> container.getNumberType())));

    numberTypes = Collections.unmodifiableSet(functionContainersByNumberType.keySet());

    numberType = numberTypes.size() == 1 ? new ArrayList<Class<? extends Number>>(numberTypes).get(0) :  null;

    LOGGER.info("\nSpecified number types: {}\nUsed number type: {}\n",   numberTypes.stream().map(c -> c.getSimpleName()).sorted().collect(Collectors.joining(", ")), numberType == null ? "Number type not defined": numberType.getSimpleName());

    functionContainers.forEach(f -> f.getMathematicalFunctions().forEach((k,v) -> LOGGER.info("\n{}\n", k)));
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

  public List<FunctionContainer<? extends Number>> getAvailableFunctionContainers(){
    return functionContainersByNumberType.get(numberType);
  }
}


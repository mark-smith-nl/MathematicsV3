package nl.smith.mathematics.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import nl.smith.mathematics.annotation.IsPublicInstanceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Primary
public class FacultyService extends RecursiveValidatedService<FacultyService> {

  private final static String SIBLING_BEAN_NAME = "FACULTYSERVICE";

  public BigInteger faculty(@Min(4) BigInteger arg) {
    if (arg.equals(BigInteger.ZERO) || arg.equals(BigInteger.ONE)) {
      return BigInteger.ONE;
    }

    return arg.multiply(sibling.faculty(arg.subtract(BigInteger.ONE)));

  }

  @Override
  public String getSiblingBeanName() {
    return SIBLING_BEAN_NAME;
  }

  @Bean(SIBLING_BEAN_NAME)
  public FacultyService makeSibling() {
    return new FacultyService();
  }

}

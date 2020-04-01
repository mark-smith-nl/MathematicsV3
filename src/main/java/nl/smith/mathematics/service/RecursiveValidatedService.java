package nl.smith.mathematics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.annotation.Validated;

@Validated
public abstract class RecursiveValidatedService<S> {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  protected S sibling;

  /** The sibling's bean name of the service */
  public abstract String getSiblingBeanName();

  public S getSibling() {
    return sibling;
  }

  public void setSibling(S sibling) {
    this.sibling = sibling;
  }

  public abstract S makeSibling();
}

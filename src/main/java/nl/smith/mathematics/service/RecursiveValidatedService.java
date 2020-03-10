package nl.smith.mathematics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.annotation.Validated;

@Validated
public abstract class RecursiveValidatedService<T> {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  protected T sibling;

  public abstract String getSiblingBeanName();

  public T getSibling() {
    return sibling;
  }

  public void setSibling(T sibling) {
    this.sibling = sibling;
  }

  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public abstract T makeSibling();
}

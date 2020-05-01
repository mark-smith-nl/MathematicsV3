package nl.smith.mathematics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

/** If service methods are recursive or call other methods with constraint annotations normally these constraints are not checked.
 * Methods constraints are only checked if the service is validated (i.e. is a proxy) and if the method is invoked by an instance other then the instance enclosing the method.
 * To solve this problem these services should implement this ({@link RecursiveValidatedService} service.
 * As a result every instant has a sibling service which should be called in case of invocation of a validated method residing in the corresponding class.
 * The association of these instances is biderectional.
 * For the injection of the sibling into the service see: {@link nl.smith.mathematics.configuration.MathematicsApplicationListener)}
 * @param <S>
 */
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

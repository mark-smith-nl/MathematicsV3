package nl.smith.mathematics.validator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import nl.smith.mathematics.annotation.IsPublicInstanceMethod;

public class IsPublicStaticInstanceMethodValidator implements ConstraintValidator<IsPublicInstanceMethod, Method> {

  @Override
  public void initialize(IsPublicInstanceMethod isPublicInstanceMethod) {
  }

  @Override
  public boolean isValid(Method method, ConstraintValidatorContext constraintValidatorContext) {
    boolean valid = true;

    if (method != null) {
      valid = !Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers());
    }

    return valid;
  }

}

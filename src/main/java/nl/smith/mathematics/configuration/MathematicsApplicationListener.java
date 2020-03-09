package nl.smith.mathematics.configuration;

import nl.smith.mathematics.service.MethodAnnotationFinderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class MathematicsApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    ApplicationContext context = contextRefreshedEvent.getApplicationContext();

    /*Map<String, Object> validatedBeans = context.getBeansWithAnnotation(
        Validated.class);
    System.out.println(validatedBeans.size());
    Map<String, Object> recursiveMethodContainingBeans = context.getBeansWithAnnotation(
        HasRecursiveValidatedMethods.class);
    System.out.println(recursiveMethodContainingBeans.size());


    Map<String, Object> v = context.getBeansWithAnnotation(
        Scope.class);

    Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(
        HasRecursiveValidatedMethods.class);
    beansWithAnnotation.values().forEach(b -> {
      System.out.println("===>" + b.getClass());
      System.out.println("===>" + b.getClass().getAnnotation(Scope.class));
    });*/
    MethodAnnotationFinderService methodAnnotationFinderService = context.getBean(MethodAnnotationFinderService.class);
    MethodAnnotationFinderService sibling = context.getBean("sibling", MethodAnnotationFinderService.class);
    methodAnnotationFinderService.setSibling(sibling);
    sibling.setSibling(methodAnnotationFinderService);
  }

}

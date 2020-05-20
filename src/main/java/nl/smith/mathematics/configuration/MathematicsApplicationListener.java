package nl.smith.mathematics.configuration;

import nl.smith.mathematics.service.RecursiveValidatedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map.Entry;

@Component
public class MathematicsApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MathematicsApplicationListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext context = contextRefreshedEvent.getApplicationContext();

        wireRecursiveValidatedService(context);
    }

    private void wireRecursiveValidatedService(ApplicationContext context) {
        context.getBeansOfType(RecursiveValidatedService.class)
                .entrySet()
                .stream()
                .filter(e -> !e.getKey().equals(e.getValue().getSiblingBeanName()))
                .map(Entry::getValue)
                .forEach(b -> {
                    RecursiveValidatedService sibling = (RecursiveValidatedService) context.getBean(b.getSiblingBeanName());
                    b.setSibling(sibling);
                    sibling.setSibling(b);
                    LOGGER.info("Wired recursive validated services of type {}.", b.getClass().getSuperclass().getCanonicalName());
                });
    }

}

package ro.mariuscirstea.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ro.mariuscirstea.template.service.EventService;

@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class TemplateApplication {

    private final EventService eventService;

    private static ConfigurableApplicationContext applicationContext;

    @Value("${ro.marius.cirstea.properties.app-name:'template'}")
    private String applicationName;

    public TemplateApplication(EventService eventService) {
        this.eventService = eventService;
    }

    public static void main(String[] args) {
        try {
            applicationContext = SpringApplication.run(TemplateApplication.class, args);
            log.info("Application Run command issued");
        } catch (Exception e) {
            log.error("Any application error is collected here: {}", e.getMessage(), e);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {
        eventService.appStarted();
        log.debug("Application {} started event triggered!", applicationName);
    }

    @EventListener(ContextClosedEvent.class)
    public void contextClosed() {
        if (applicationContext.isActive()) {
            eventService.appStopped();
        }
        log.debug("Application {} stopped event triggered! Was the app context still active? -> {}", applicationName, applicationContext.isActive());
    }

}

package ro.mariuscirstea.eventtracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
//@EnableScheduling
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class EventTrackerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventTrackerApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(EventTrackerApplication.class, args);
            LOGGER.debug("Application EventTracker started!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

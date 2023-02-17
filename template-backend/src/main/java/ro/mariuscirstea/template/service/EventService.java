package ro.mariuscirstea.template.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import ro.mariuscirstea.template.entity.EVT_Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EventService {

    ObjectMapper objectMapper = new ObjectMapper();

    EVT_Event saveEvent(EVT_Event evt_event);

    EVT_Event saveExtendedEvent(String type, String description, String ip, String jsonAdditionalDetails, Long userId);

    List<EVT_Event> getLatestEvents(Integer noOfEvents);

    Page<EVT_Event> getFilteredEvents(Specification<EVT_Event> specification, PageRequest pageRequest);

    default void saveSystemEvent(String type, String description) {
        saveExtendedEvent(type, description, null, null, 0L);
    }

    default void appStarted() {
        saveSystemEvent("APP_START", "Application started");
    }

    default void appStopped() {
        saveSystemEvent("APP_STOP", "Application stopped");
    }

    default void invalidPassword(String email, String userAgent, String ip) {
        saveLogin("Invalid password provided", email, userAgent, ip, 0L);
    }

    default void invalidLoginPrincipal(String email, String userAgent, String ip) {
        saveLogin("Invalid email or username provided", email, userAgent, ip, 0L);
    }

    default void saveLogin(String failureReason, String email, String userAgent, String ip, Long userId) {
        Map<String, Object> details = new HashMap<>();
        details.put("userAgent", userAgent);
        details.put("email", email);
        try {
            saveExtendedEvent("LOGIN", failureReason, ip, objectMapper.writeValueAsString(details), userId);
        } catch (JsonProcessingException ignored) {
        }
    }

    default void loginOk(String email, String userAgent, String ip, Long userId) {
        saveLogin("User authenticated successfully", email, userAgent, ip, userId);
    }
}

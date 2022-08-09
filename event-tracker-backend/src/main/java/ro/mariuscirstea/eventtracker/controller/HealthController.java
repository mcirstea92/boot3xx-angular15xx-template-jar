package ro.mariuscirstea.eventtracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController("health")
@RequestMapping("health")
@Slf4j
public class HealthController {

    @GetMapping("status")
    public ResponseEntity<Map<String, String>> status(HttpServletRequest servletRequest) {
        log.info("Servlet request on status endpoint: {}", servletRequest.toString());
        return ResponseEntity.ok(Collections.singletonMap("status", "active"));
    }

}

package ro.mariuscirstea.eventtracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.error("Unauthorized error. Message - {}", e.getMessage());
        Map<String, Object> responseMap = new HashMap<>();
        response.setStatus(401);
        responseMap.put("error", true);
        responseMap.put("message", "Unauthorized");
        response.setHeader("content-type", "application/json");
        String responseMsg = objectMapper.writeValueAsString(responseMap);
        response.getWriter().write(responseMsg);
    }
}

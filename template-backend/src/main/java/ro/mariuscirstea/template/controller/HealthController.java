package ro.mariuscirstea.template.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.mariuscirstea.template.security.IsAdmin;
import ro.mariuscirstea.template.security.IsAdminOrUserOrDemo;
import ro.mariuscirstea.template.security.IsDemo;
import ro.mariuscirstea.template.security.IsUser;
import ro.mariuscirstea.template.security.jwt.JwtProvider;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@RestController("health")
@RequestMapping("health")
@Slf4j
public class HealthController {

    private final JwtProvider jwtProvider;

    public HealthController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


    @GetMapping("status")
    public ResponseEntity<Map<String, String>> status() {
        return ResponseEntity.ok(Collections.singletonMap("status", "active"));
    }

    @GetMapping("statusAuth")
    @IsAdminOrUserOrDemo
    public ResponseEntity<Map<String, String>> statusAuth() {
        return ResponseEntity.ok(Collections.singletonMap("status", "active"));
    }

    @GetMapping("onlyForUser")
    @IsUser
    public ResponseEntity<Map<String, String>> onlyForUser() {
        return ResponseEntity.ok(Collections.singletonMap("status", "active"));
    }

    @GetMapping("onlyForAdmin")
    @IsAdmin
    public ResponseEntity<Map<String, String>> onlyForAdmin(HttpServletRequest request) {
        String auth = request.getHeader("Authorization").substring(7);
        Date date = jwtProvider.getExpirationFromJwtToken(auth);
        return ResponseEntity.ok(Collections.singletonMap("status", "active till " + date.toString()));
    }

    @GetMapping("onlyForDemo")
    @IsDemo
    public ResponseEntity<Map<String, String>> onlyForDemo() {
        return ResponseEntity.ok(Collections.singletonMap("status", "active"));
    }

}

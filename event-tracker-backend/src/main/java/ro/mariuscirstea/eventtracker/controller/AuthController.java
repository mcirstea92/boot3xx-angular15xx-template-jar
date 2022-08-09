package ro.mariuscirstea.eventtracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.mariuscirstea.eventtracker.entity.User;
import ro.mariuscirstea.eventtracker.model.RequestDetails;
import ro.mariuscirstea.eventtracker.model.auth.JwtResponse;
import ro.mariuscirstea.eventtracker.model.auth.LoginForm;
import ro.mariuscirstea.eventtracker.repository.UserRepository;
import ro.mariuscirstea.eventtracker.security.JwtProvider;
import ro.mariuscirstea.eventtracker.utils.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RequestUtils requestUtils;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder encoder, JwtProvider jwtProvider,
                          RequestUtils requestUtils, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = encoder;
        this.jwtProvider = jwtProvider;
        this.requestUtils = requestUtils;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginRequest, HttpServletRequest request) {
        RequestDetails requestDetails = requestUtils.extractReqDetails(request);
        boolean isValid = false;
        Optional<User> user = this.userRepository.findByEmail(loginRequest.getEmail());
        if (user.isPresent()) {
            requestDetails.setUser(user.get());
            isValid = passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword());
        }
        if (!isValid) {
            //Wrong username or password status
            return new ResponseEntity<>("{\"error\": \"Incorrect username or password!\"}", HttpStatus.UNAUTHORIZED);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt, user.get().getUsername(), user.get().getEmail(),
                user.get().getName(), userDetails.getAuthorities()));
    }

}

package ro.mariuscirstea.template.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
import ro.mariuscirstea.template.entity.ROL_Role;
import ro.mariuscirstea.template.entity.RefreshToken;
import ro.mariuscirstea.template.entity.USR_User;
import ro.mariuscirstea.template.exception.TokenRefreshException;
import ro.mariuscirstea.template.model.ERole;
import ro.mariuscirstea.template.model.RequestDetails;
import ro.mariuscirstea.template.payload.TokenRefreshRequest;
import ro.mariuscirstea.template.payload.auth.JwtResponse;
import ro.mariuscirstea.template.payload.auth.LoginForm;
import ro.mariuscirstea.template.payload.auth.SignupRequest;
import ro.mariuscirstea.template.payload.response.MessageResponse;
import ro.mariuscirstea.template.payload.response.TokenRefreshResponse;
import ro.mariuscirstea.template.repository.RoleRepository;
import ro.mariuscirstea.template.repository.UserRepository;
import ro.mariuscirstea.template.security.UserPrincipal;
import ro.mariuscirstea.template.security.jwt.JwtProvider;
import ro.mariuscirstea.template.service.EventService;
import ro.mariuscirstea.template.service.RefreshTokenService;
import ro.mariuscirstea.template.utils.RequestUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    private final RoleRepository roleRepository;
    private final EventService eventService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder encoder, JwtProvider jwtProvider,
                          RequestUtils requestUtils, UserRepository userRepository, RoleRepository roleRepository,
                          EventService eventService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = encoder;
        this.jwtProvider = jwtProvider;
        this.requestUtils = requestUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.eventService = eventService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginRequest, HttpServletRequest request) {
        RequestDetails requestDetails = requestUtils.extractReqDetails(request);
        boolean isValid = false;
        Optional<USR_User> user = this.userRepository.findUSR_UserByUsername(loginRequest.getUser());
        if (user.isEmpty()) {
            user = this.userRepository.findByEmail(loginRequest.getUser());
        }
        if (user.isPresent()) {
            requestDetails.setUser(user.get());
            isValid = passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword());
            if (!isValid) {
                eventService.invalidPassword(loginRequest.getUser(), requestDetails.getUserAgent(), requestDetails.getIp());
                return new ResponseEntity<>(Collections.singletonMap("error", "Incorrect password!"), HttpStatus.UNAUTHORIZED);
            }
        }
        if (!isValid) {
            //Wrong username or password status
            eventService.invalidLoginPrincipal(loginRequest.getUser(), requestDetails.getUserAgent(), requestDetails.getIp());
            return new ResponseEntity<>(Collections.singletonMap("error", "Invalid username or email!"), HttpStatus.UNAUTHORIZED);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUser(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        eventService.loginOk(loginRequest.getUser(), requestDetails.getUserAgent(), requestDetails.getIp(), requestDetails.getUser().getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(requestDetails.getUser().getId());
        return ResponseEntity.ok(new JwtResponse(jwt, user.get().getUsername(), user.get().getEmail(),
                user.get().getName(), userDetails.getAuthorities(), requestDetails.getUser().getId().toString(), refreshToken.getToken()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        USR_User user = new USR_User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<ROL_Role> roles = new HashSet<>();

        if (strRoles == null) {
            ROL_Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ADMIN" -> {
                        ROL_Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "MOD" -> {
                        ROL_Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    case "DEMO" -> {
                        ROL_Role demoRole = roleRepository.findByName(ERole.ROLE_DEMO)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(demoRole);
                    }
                    default -> {
                        ROL_Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest body, HttpServletRequest request) {
        log.info("Executing refresh token");
        String requestRefreshToken = body.getRefreshToken();
        RequestDetails requestDetails = requestUtils.extractReqDetails(request);
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(requestRefreshToken);

        if (optionalRefreshToken.isPresent()) {
            RefreshToken refreshToken;
            try {
                refreshToken = refreshTokenService.verifyExpiration(optionalRefreshToken.get());
                USR_User user = refreshToken.getUser();
                refreshTokenService.clearOtherTokens(requestDetails.getUser().getId(), requestRefreshToken);
                String token = jwtProvider.generateTokenFromUsername(user.getUsername());
                return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            } catch (TokenRefreshException tokenException) {
                return ResponseEntity.internalServerError().body(new MessageResponse("Refresh token availability expired. Please login again!"));
            }
        }
        log.error("Refresh token {} is not in database", requestRefreshToken);
        return ResponseEntity.internalServerError().body(new MessageResponse("Refresh token is not in database"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

}

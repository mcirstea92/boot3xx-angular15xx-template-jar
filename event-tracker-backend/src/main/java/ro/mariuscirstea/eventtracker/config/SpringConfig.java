package ro.mariuscirstea.eventtracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import ro.mariuscirstea.eventtracker.security.JwtAuthTokenFilter;
import ro.mariuscirstea.eventtracker.security.JwtProvider;
import ro.mariuscirstea.eventtracker.service.impl.UserServiceImpl;


@Configuration
public class SpringConfig {

    private final JwtProvider jwtProvider;
    private final UserServiceImpl userDetailsService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfig.class);
    private final ApplicationContext context;

    public SpringConfig(JwtProvider jwtProvider, UserServiceImpl userDetailsService, ApplicationContext context) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.context = context;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter(jwtProvider, userDetailsService);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapperWithNonNulls = new ObjectMapper();
        return mapperWithNonNulls;
    }

}


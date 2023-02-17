package ro.mariuscirstea.template.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ro.mariuscirstea.template.filters.AccessDeniedHandler403;
import ro.mariuscirstea.template.filters.JwtAuthTokenFilter;
import ro.mariuscirstea.template.security.jwt.JwtAuthEntryPoint;
import ro.mariuscirstea.template.security.jwt.JwtProvider;
import ro.mariuscirstea.template.service.impl.UserServiceImpl;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class WebSecurityConfig {

    private final UserServiceImpl userDetailsService;

    private final JwtAuthEntryPoint unauthorizedHandler;

    private final JwtProvider jwtProvider;

    public WebSecurityConfig(UserServiceImpl userDetailsService, JwtAuthEntryPoint jwtAuthEntryPoint, JwtProvider jwtProvider) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = jwtAuthEntryPoint;
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler403();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(configurationSource())
                .and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/accessDenied").permitAll()
                .requestMatchers("/health/**").permitAll()
                .requestMatchers("/configs/public/**").permitAll()
                .requestMatchers("/webapp/**").permitAll()
                .requestMatchers("/static/**").anonymous()
                .requestMatchers("/public/**").anonymous()
                .requestMatchers("/generated/**").anonymous()
                .requestMatchers("/index.html").anonymous()
                .requestMatchers("/favicon.ico").anonymous()
                .anyRequest().permitAll()
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and().logout().logoutUrl("/auth/signout").logoutSuccessUrl("/login.html");
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    private CorsConfigurationSource configurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter(jwtProvider, userDetailsService);
    }

}


package ro.mariuscirstea.eventtracker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ro.mariuscirstea.eventtracker.service.impl.UserServiceImpl;

import java.util.Arrays;
import java.util.Collections;

@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userDetailsService;

    private final JwtAuthEntryPoint unauthorizedHandler;

    private final PasswordEncoder passwordEncoder;

    private final JwtAuthTokenFilter authJwtTokenFilter;

    public WebSecurityConfig(UserServiceImpl userService, JwtAuthEntryPoint jwtAuthEntryPoint,
                             PasswordEncoder passwordEncoder, JwtAuthTokenFilter authJwtTokenFilter) {
        this.userDetailsService = userService;
        this.unauthorizedHandler = jwtAuthEntryPoint;
        this.passwordEncoder = passwordEncoder;
        this.authJwtTokenFilter = authJwtTokenFilter;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(configurationSource())
                .and().csrf().disable()
                .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/health/**").permitAll()
                /*.antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()*/
                .antMatchers("/static/**").anonymous()
                .antMatchers("/generated/**").anonymous()
                .antMatchers("/index.html").anonymous()
                .antMatchers("/favicon.ico").anonymous()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/auth/login").permitAll()
                .and().logout().permitAll()
                .and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
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

}


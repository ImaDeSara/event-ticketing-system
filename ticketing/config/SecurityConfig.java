package com.ticketing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
/**
 * Configures security settings for the backend application.
 * Provides basic authentication and controls access to different endpoints.
 * Disables CSRF protection to allow interactions with non-browser clients (e.g., Postman).
 */
@Configuration
public class SecurityConfig {
    /**
     * Configures the security filter chain to define access control rules and disable CSRF protection.
     * @param http The {@link HttpSecurity} object for customizing security settings.
     * @return A configured {@link SecurityFilterChain}.
     * @throws Exception if there is an issue configuring the security settings.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/tickets/**").permitAll() // Allow public access to all endpoints under /api/tickets
                        .anyRequest().authenticated() // Require authentication for all other endpoints
                )
                .csrf(csrf -> csrf.disable()); // Explicitly disable CSRF protection
        return http.build();
    }
}



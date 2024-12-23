package com.ticketing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
/**
 * Configures Cross-Origin Resource Sharing (CORS) for the backend application.
 * This configuration allows the frontend (running on a different origin, such as http://localhost:4200)
 * to interact with the backend by enabling CORS with flexible settings.
 */
@Configuration
public class CorsConfig {
    /**
     * Creates a {@link CorsFilter} bean to handle CORS settings for incoming requests.
     * @return A {@link CorsFilter} configured to allow specific origins, headers, and methods.
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // Allow credentials (e.g., cookies or authentication headers)
        config.setAllowCredentials(true);
        // Allow requests from the specified frontend origin
        config.addAllowedOrigin("http://localhost:4200");
        // Allow all HTTP headers in requests
        config.addAllowedHeader("*");
        // Allow all HTTP methods (e.g., GET, POST, PUT, DELETE)
        config.addAllowedMethod("*");
        // Apply the CORS configuration to all API endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}


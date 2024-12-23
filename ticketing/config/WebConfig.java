package com.ticketing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * Configuration class to handle Cross-Origin Resource Sharing (CORS) settings for the application.
 * Allows the Angular frontend (running on a different origin) to interact with the Spring Boot backend.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Configures CORS mappings for the application.
     * @param registry The {@link CorsRegistry} to customize allowed origins, methods, and headers.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all paths
                .allowedOrigins("http://localhost:4200") // Allow Angular frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow cookies or authentication headers
    }
}
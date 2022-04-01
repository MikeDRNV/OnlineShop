package com.akvelon.dorodnikov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Allows restricted resources on a web page to be requested from another domain.
 */
@Configuration
public class CorsConfiguration {

    /**
     * Allows to configure "global" cross origin request processing.
     *
     * @return Java configuration for Spring MVC.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
            }
        };
    }
}
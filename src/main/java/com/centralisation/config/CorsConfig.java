package com.centralisation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorsConfig.class);

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                LOGGER.info("Début de la configuration des règles CORS...");
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
                LOGGER.info("CORS configuré avec succès :");
                LOGGER.info(" - Origines autorisées : http://localhost:4200");
                LOGGER.info(" - Méthodes autorisées : GET, POST, PUT, DELETE, OPTIONS");
                LOGGER.info(" - En-têtes autorisés : Tous (*)");
                LOGGER.info(" - Autorisation des credentials : Activé");
            }
        };
    }
}

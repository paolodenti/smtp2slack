package com.github.paolodenti.smtp2slack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;

/**
 * ObjectMapper configuration.
 */
public class JacksonConfig {

    /**
     * Jackson objectMapper configuration.
     *
     * @return the Jackson objectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}

package com.vuvarov.rashod.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfiguration {
    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {

            @Override
            public void configure(ObjectMapper objectMapper) {
                super.configure(objectMapper);
                objectMapper.registerModule(new Jdk8Module());
                objectMapper.registerModule(new JavaTimeModule());
            }

        };
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return builder;
    }

}

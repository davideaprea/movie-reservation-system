<<<<<<<< HEAD:src/main/java/com/mrs/app/config/ObjectMapperConfig.java
package com.mrs.app.config;
========
package com.mrs.app.shared.configuration;
>>>>>>>> refactor:src/main/java/com/mrs/app/shared/configuration/ObjectMapperConfig.java

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        return mapper;
    }
}

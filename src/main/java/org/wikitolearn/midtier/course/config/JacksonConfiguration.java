package org.wikitolearn.midtier.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonConfiguration {
  @Bean
  public ObjectMapper objectMapper() {
      return new ObjectMapper();
  }
}

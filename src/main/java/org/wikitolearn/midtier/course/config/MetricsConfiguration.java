package org.wikitolearn.midtier.course.config;

import org.springframework.boot.actuate.metrics.web.client.RestTemplateExchangeTagsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wikitolearn.midtier.course.actuate.metrics.web.client.CustomRestTemplateExchangeTagsProvider;

@Configuration
public class MetricsConfiguration {
  @Bean
  public RestTemplateExchangeTagsProvider restTemplateExchangeTagsProvider() {
    return new CustomRestTemplateExchangeTagsProvider();
  }
}

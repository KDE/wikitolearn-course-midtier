package org.wikitolearn.midtier.course.actuate.health;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;

@Component
public class RestClientHealthIndicator implements HealthIndicator {

  private MeterRegistry registry;
  
  private final List<String> notableStatusTags;
  private final List<String> ignoredUriTags;
  
  @Autowired
  public RestClientHealthIndicator(MeterRegistry registry) {
    this.registry = registry;
    this.notableStatusTags = new ArrayList<>();
    this.notableStatusTags.add("500");
    this.notableStatusTags.add("503");
    this.notableStatusTags.add("CLIENT_ERROR");
    this.ignoredUriTags = new ArrayList<>();
  }
  
  @Override
  public Health health() {

    if (registry.find("http.client.requests").meters().isEmpty()) {
      return Health.up().build();
    } else {
      double clientTotal = registry.get("http.client.requests").timer().count();
      
      double errorCounter = this.calculateErrorCounter();
      double percentage = (errorCounter / clientTotal) * 100;
      
      if(percentage > 90 && clientTotal > 100) {
        return Health.down().build(); 
      }
      
      return Health.up().build();
    }
  }
  
  private double calculateErrorCounter() {
    double notableValue = 0.0;
    double ignoredValue = 0.0;
    
    for(String value : notableStatusTags) {
      notableValue += registry.find("http.client.requests").tag("status", value).meters().isEmpty()
      ? 0.0
      : registry.get("http.client.requests").tag("status", value).timer().count();
    }
    
    for(String value : ignoredUriTags) {
      ignoredValue += registry.find("http.client.requests").tag("uri", value).meters().isEmpty()
      ? 0.0
      : registry.get("http.client.requests").tag("uri", value).timer().count();
    }
    
    return notableValue - ignoredValue;
  }
}
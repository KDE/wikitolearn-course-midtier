package org.wikitolearn.midtier.course.actuate.health;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;

@Component
public class RestControllerHealthIndicator implements HealthIndicator {
  
  private MeterRegistry registry;
  private final List<String> notableStatusTags;
  private final List<String> ignoredUriTags;
  
  @Autowired
  public RestControllerHealthIndicator(MeterRegistry registry) {
    this.registry = registry;
    this.notableStatusTags = new ArrayList<>();
    this.notableStatusTags.add("500");
    this.notableStatusTags.add("503");
    this.ignoredUriTags = new ArrayList<>();
    this.ignoredUriTags.add("/_meta/status");
    this.ignoredUriTags.add("/**/favicon.ico");
  }

  @Override
  public Health health() {

    if (registry.find("http.server.requests").meters().isEmpty()) {
      return Health.up().build();
    } else {
      
      double controllerTotal = registry.get("http.server.requests").timer().count();
      double errorCounter = this.calculateErrorCounter();
      double percentage = (errorCounter / controllerTotal) * 100;
      
      if(percentage > 90 && controllerTotal > 100) {
        return Health.down().build(); 
      }
      
      return Health.up().build();
    }
  }
  
  private double calculateErrorCounter() {
    double notableValue = 0.0;
    double ignoredValue = 0.0;
    
    for(String value : notableStatusTags) {
      notableValue += registry.find("http.server.requests").tag("status", value).meters().isEmpty()
      ? 0.0
      : registry.get("http.server.requests").tag("status", value).timer().count();
    }
    
    for(String value : ignoredUriTags) {
      ignoredValue += registry.find("http.server.requests").tag("uri", value).meters().isEmpty()
      ? 0.0
      : registry.get("http.server.requests").tag("uri", value).timer().count();
    }
    
    return notableValue - ignoredValue;
  }
}
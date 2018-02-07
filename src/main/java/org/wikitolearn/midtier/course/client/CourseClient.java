package org.wikitolearn.midtier.course.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class CourseClient {

  private final RestTemplate client;

  @Value("${application.clients.courses-manager}")
  private String baseUrl;

  public CourseClient() {
    this.client = new RestTemplate();
  }

  @HystrixCommand(fallbackMethod = "defaultCourses")
  public String findAll() {
    return client.getForObject(baseUrl + "/courses", String.class);
  }

  public String find(String courseId) {
    return client.getForObject(baseUrl + "/courses/" + courseId, String.class);
  }

  private String defaultCourses() {
    return "Hello default!";
  }
}

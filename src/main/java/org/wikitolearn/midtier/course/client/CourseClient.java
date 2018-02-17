package org.wikitolearn.midtier.course.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.wikitolearn.midtier.course.entity.Course;
import org.wikitolearn.midtier.course.entity.EntityList;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CourseClient {

  private final RestTemplate client;

  @Value("${application.clients.courses-backend}")
  private String baseUrl;

  public CourseClient() {
    this.client = new RestTemplate();
  }

  @HystrixCommand(fallbackMethod = "defaultCourses")
  public EntityList<Course> findAll() {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/courses").build().encode().toUri();
    return client
        .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<EntityList<Course>>() {
        }).getBody();
  }

  public Course find(String courseId, MultiValueMap<String, String> params) {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/courses/" + courseId)
        .queryParams(params)
        .build()
        .encode()
        .toUri();
    return client.getForObject(uri, Course.class);
  }

  private String defaultCourses() {
    return "Hello default!";
  }
}
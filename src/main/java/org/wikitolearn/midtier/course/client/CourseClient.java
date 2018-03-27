package org.wikitolearn.midtier.course.client;

import java.net.URI;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.wikitolearn.midtier.course.entity.Course;
import org.wikitolearn.midtier.course.entity.EntityList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CourseClient {

  private final RestTemplate client;

  @Value("${application.clients.courses-backend}")
  private String baseUrl;

  public CourseClient(RestTemplateBuilder restTemplateBuilder) {
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    this.client = restTemplateBuilder.requestFactory(() -> requestFactory).build();
  }

  @HystrixCommand(fallbackMethod = "defaultCourses")
  public EntityList<Course> findAll() {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/courses")
        .build()
        .encode()
        .toUri();
    
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
  
  public EntityList<Course> findByChapterId(String chapterId) {
    MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
    query.add("where", "{\"chapters._id\":\"" + chapterId + "\"}");
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/courses")
        .queryParams(query)
        .build()
        .encode()
        .toUri();
    
    return client
        .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<EntityList<Course>>() {
        }).getBody();
  }
  
  public Course save(Course course) throws JsonProcessingException {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/courses")
        .build()
        .encode()
        .toUri();
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    HttpEntity <String> httpEntity = new HttpEntity <String> (course.toSchemaCompliant(), headers);
    return client.postForObject(uri, httpEntity, Course.class);
  }
  
  public Course update(Course course) throws JsonProcessingException {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/courses/" + course.getId())
        .build()
        .encode()
        .toUri();
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setIfMatch(course.getEtag());
    
    HttpEntity <String> httpEntity = new HttpEntity <String> (course.toSchemaCompliant(), headers);
    return client.patchForObject(uri, httpEntity, Course.class);
  }

  private String defaultCourses() {
    return "Hello default!";
  }
}

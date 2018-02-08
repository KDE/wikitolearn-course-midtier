package org.wikitolearn.midtier.course.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.wikitolearn.midtier.course.entity.Chapter;
import org.wikitolearn.midtier.course.entity.EntityList;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChapterClient {

  private final RestTemplate client;

  @Value("${application.clients.chapters-backend}")
  private String baseUrl;

  public ChapterClient() {
    this.client = new RestTemplate();
  }

  @HystrixCommand(fallbackMethod = "defaultChapters")
  public EntityList<Chapter> findAll() {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/chapters").build().encode().toUri();
    return client
        .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<EntityList<Chapter>>() {
        }).getBody();
  }

  public Chapter find(String chapterId, MultiValueMap<String, String> params) {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/chapters/" + chapterId)
        .queryParams(params)
        .build()
        .encode()
        .toUri();
    return client.getForObject(uri, Chapter.class);
  }

  private String defaultChapters() {
    return "Hello default!";
  }
}

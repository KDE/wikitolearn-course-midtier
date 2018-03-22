package org.wikitolearn.midtier.course.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.wikitolearn.midtier.course.entity.Chapter;
import org.wikitolearn.midtier.course.entity.EntityList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChapterClient {

  private final RestTemplate client;

  @Value("${application.clients.chapters-backend}")
  private String baseUrl;

  public ChapterClient(RestTemplateBuilder restTemplateBuilder) {
    this.client = restTemplateBuilder.requestFactory(new HttpComponentsClientHttpRequestFactory()).build();
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
  
  public EntityList<Chapter> findByPageId(String pageId) {
    MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
    query.add("where", "{\"pages._id\":\"" + pageId + "\"}");
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/chapters")
        .queryParams(query)
        .build()
        .encode()
        .toUri();
    
    return client
        .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<EntityList<Chapter>>() {
        }).getBody();
  }
  
  public Chapter store(Chapter chapter) throws JsonProcessingException {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/chapters")
        .build()
        .encode()
        .toUri();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity <String> httpEntity = new HttpEntity <String> (chapter.toSchemaCompliant(), headers);
    return client.postForObject(uri, httpEntity, Chapter.class);
  }
  
  public Chapter update(Chapter chapter) throws JsonProcessingException {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/chapters/" + chapter.getId())
        .build()
        .encode()
        .toUri();
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setIfMatch(chapter.getEtag());
  
    
    HttpEntity <String> httpEntity = new HttpEntity <String> (chapter.toSchemaCompliant(), headers);
    return client.patchForObject(uri, httpEntity, Chapter.class);
  }

  private String defaultChapters() {
    return "Hello default!";
  }
}

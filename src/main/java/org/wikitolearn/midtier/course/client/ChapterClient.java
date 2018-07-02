package org.wikitolearn.midtier.course.client;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.wikitolearn.midtier.course.entity.dto.out.UpdateOrSaveChapterClientDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChapterClient {

  private final RestTemplate client;

  @Value("${application.clients.chapters-backend}")
  private String baseUrl;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ModelMapper modelMapper;

  public ChapterClient(RestTemplateBuilder restTemplateBuilder) {
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    this.client = restTemplateBuilder.requestFactory(() -> requestFactory).build();
  }

  @HystrixCommand(fallbackMethod = "defaultChapters")
  public EntityList<Chapter> findAll() {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/chapters").build().encode().toUri();
    return client.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<EntityList<Chapter>>() {
    }).getBody();
  }

  public Chapter find(String chapterId, MultiValueMap<String, String> params) {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/chapters/" + chapterId).queryParams(params).build().encode()
        .toUri();
    return client.getForObject(uri, Chapter.class);
  }

  public EntityList<Chapter> findByPageId(String pageId) throws JsonProcessingException {
    MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
    ObjectNode whereJsonObject = objectMapper.getNodeFactory().objectNode().put("pages._id", pageId);
    query.add("where", objectMapper.writeValueAsString(whereJsonObject));

    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/chapters").queryParams(query).build().encode().toUri();

    return client.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<EntityList<Chapter>>() {
    }).getBody();
  }

  public Chapter store(Chapter chapter) throws JsonProcessingException {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/chapters").build().encode().toUri();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<UpdateOrSaveChapterClientDto> httpEntity = new HttpEntity<>(
        modelMapper.map(chapter, UpdateOrSaveChapterClientDto.class), headers);
    return client.postForObject(uri, httpEntity, Chapter.class);
  }

  public Chapter update(Chapter chapter) throws JsonProcessingException {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/chapters/" + chapter.getId()).build().encode().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setIfMatch(chapter.getEtag());

    HttpEntity<UpdateOrSaveChapterClientDto> httpEntity = new HttpEntity<>(
        modelMapper.map(chapter, UpdateOrSaveChapterClientDto.class), headers);
    return client.patchForObject(uri, httpEntity, Chapter.class);
  }

  public Chapter delete(Chapter chapter) throws JsonProcessingException {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/chapters/" + chapter.getId()).build().encode().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setIfMatch(chapter.getEtag());

    HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
    return client.exchange(uri, HttpMethod.DELETE, httpEntity, Chapter.class).getBody();
  }

  private String defaultChapters() {
    return "Hello default!";
  }
}

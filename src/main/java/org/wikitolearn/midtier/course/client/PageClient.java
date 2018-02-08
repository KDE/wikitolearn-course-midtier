package org.wikitolearn.midtier.course.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.wikitolearn.midtier.course.entity.EntityList;
import org.wikitolearn.midtier.course.entity.Page;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PageClient {

    private final RestTemplate client;

    @Value("${application.clients.pages-backend}")
    private String baseUrl;

    public PageClient() {
        this.client = new RestTemplate();
    }

    @HystrixCommand(fallbackMethod = "defaultPages")
    public EntityList<Page> findAll() {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/pages").build().encode().toUri();
        return client.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<EntityList<Page>>() {
        }).getBody();
    }

    public Page find(String pageId, MultiValueMap<String, String> params) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/pages/" + pageId)
            .queryParams(params)
            .build()
            .encode()
            .toUri();
        return client.getForObject(uri, Page.class);
    }

    private String defaultPages() {
        return "Hello default!";
    }

}

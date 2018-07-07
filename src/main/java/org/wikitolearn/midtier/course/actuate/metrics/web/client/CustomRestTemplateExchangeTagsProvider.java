package org.wikitolearn.midtier.course.actuate.metrics.web.client;

import java.util.Arrays;

import org.springframework.boot.actuate.metrics.web.client.RestTemplateExchangeTags;
import org.springframework.boot.actuate.metrics.web.client.RestTemplateExchangeTagsProvider;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import io.micrometer.core.instrument.Tag;

public class CustomRestTemplateExchangeTagsProvider implements RestTemplateExchangeTagsProvider {

  @Override
  public Iterable<Tag> getTags(String urlTemplate, HttpRequest request, ClientHttpResponse response) {
    return Arrays.asList(RestTemplateExchangeTags.method(request), RestTemplateExchangeTags.status(response),
        RestTemplateExchangeTags.clientName(request));
  }
}

package org.wikitolearn.midtier.course.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wikitolearn.midtier.course.entity.Page;
import org.wikitolearn.midtier.course.service.PageService;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value =  "/pages")
public class PageController {
  
  @Autowired
  private PageService pageService;
  
  @GetMapping(value = "/{pageId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Page getPage(@PathVariable String pageId) {
    return pageService.find(pageId, null);
  }
  
  @PatchMapping(value = "/{pageId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Page update(@PathVariable("pageId") String pageId, @RequestBody Page page, @RequestHeader("If-Match") String etag) throws JsonProcessingException {
    page.setId(pageId);
    page.setEtag(etag);
    return pageService.update(page);
  }
}

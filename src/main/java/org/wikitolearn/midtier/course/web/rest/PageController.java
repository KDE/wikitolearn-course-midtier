package org.wikitolearn.midtier.course.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wikitolearn.midtier.course.entity.Page;
import org.wikitolearn.midtier.course.service.PageService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value =  "/pages")
public class PageController {
  
  @Autowired
  private PageService pageService;
  
  @GetMapping(value = "/{pageId}", produces = "application/json")
  public Page getPage(@PathVariable String pageId) {
    return pageService.find(pageId, null);
  }
}

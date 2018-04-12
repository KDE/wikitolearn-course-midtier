package org.wikitolearn.midtier.course.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wikitolearn.midtier.course.entity.Chapter;
import org.wikitolearn.midtier.course.exception.InvalidResourceCreateException;
import org.wikitolearn.midtier.course.service.ChapterService;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value =  "/chapters")
public class ChapterController {
  
  @Autowired
  private ChapterService chapterService;
  
  @PatchMapping(value = "/{chapterId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Chapter update(@PathVariable("chapterId") String chapterId, @RequestBody Chapter chapter, @RequestHeader("If-Match") String etag) throws JsonProcessingException {
    chapter.setId(chapterId);
    chapter.setEtag(etag);
    return chapterService.update(chapter);
  }
  
  @PatchMapping(value = "/{chapterId}/pages", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Chapter updatePages(@PathVariable("chapterId") String chapterId, @RequestBody Chapter chapter, @RequestHeader("If-Match") String etag) throws JsonProcessingException, InvalidResourceCreateException {
    chapter.setId(chapterId);
    chapter.setEtag(etag);
    return chapterService.updatePages(chapter);
  }
  
  @PostMapping(value = "/{chapterId}/pages", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Chapter addPages(@PathVariable("chapterId") String chapterId, @RequestBody Chapter chapter, @RequestHeader("If-Match") String etag) throws JsonProcessingException, InvalidResourceCreateException {
    chapter.setId(chapterId);
    chapter.setEtag(etag);
    return chapterService.addPages(chapter);
  }
  
  @DeleteMapping(value = "/{chapterId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Chapter delete(@PathVariable("chapterId") String chapterId, @RequestHeader("If-Match") String etag) throws JsonProcessingException {
    Chapter chapter = new Chapter();
    chapter.setId(chapterId);
    chapter.setEtag(etag);
    return chapterService.delete(chapter, false);
  }
}

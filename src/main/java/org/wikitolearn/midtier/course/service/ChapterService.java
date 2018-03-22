package org.wikitolearn.midtier.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.wikitolearn.midtier.course.client.ChapterClient;
import org.wikitolearn.midtier.course.entity.Chapter;
import org.wikitolearn.midtier.course.entity.EntityList;
import org.wikitolearn.midtier.course.entity.Page;
import org.wikitolearn.midtier.course.event.ChapterUpdated;
import org.wikitolearn.midtier.course.event.PageUpdated;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChapterService {
  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  private ChapterClient chapterClient;
  
  @Autowired
  private PageService pageService;

  public EntityList<Chapter> findAll() {
    return chapterClient.findAll();
  }

  public Chapter find(String chapterId, MultiValueMap<String, String> params) {
    return chapterClient.find(chapterId, params);
  }
  
  public List<Chapter> save(List<Chapter> chapters) {
    chapters.stream().forEach(c -> {
      Chapter saved;
      try {
        saved = this.store(c);
        c.setId(saved.getId());
        c.setVersion(saved.getVersion());
      } catch (JsonProcessingException e) {
        log.error(e.getMessage());
      }
    });
    return chapters;
  }
  
  public Chapter findByPageId(String pageId) {
    EntityList<Chapter> chapters = chapterClient.findByPageId(pageId);
    return chapters.getItems().get(0);
  }
  
  public Chapter store(Chapter chapter) throws JsonProcessingException {
    if(chapter.getPages() != null && !chapter.getPages().isEmpty()) {
      chapter.setPages(pageService.save(chapter.getPages()));
    }
    chapter.setLanguage("it");
    return chapterClient.store(chapter);
  }
  
  public Chapter update(Chapter chapter) throws JsonProcessingException {
    Chapter updatedChapter = chapterClient.update(chapter);
    applicationEventPublisher.publishEvent(new ChapterUpdated(this, updatedChapter));
    return updatedChapter;
  }
  
  @EventListener
  public void handlePageUpdatedEvent(PageUpdated event) throws JsonProcessingException {
    Page updatedPage = event.getPage();    
    Chapter chapter = this.findByPageId(updatedPage.getId());
    
    chapter.getPages().stream().forEach(c -> {
      if(c.getId().equals(updatedPage.getId())) {
        c.setVersion(updatedPage.getLatestVersion());
      }
    });
    
    this.update(chapter);
  }
}

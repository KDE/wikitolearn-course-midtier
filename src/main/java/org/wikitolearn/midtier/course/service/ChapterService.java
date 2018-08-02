package org.wikitolearn.midtier.course.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.wikitolearn.midtier.course.client.ChapterClient;
import org.wikitolearn.midtier.course.entity.Chapter;
import org.wikitolearn.midtier.course.entity.Course;
import org.wikitolearn.midtier.course.entity.EntityList;
import org.wikitolearn.midtier.course.entity.Page;
import org.wikitolearn.midtier.course.event.ChapterDeleted;
import org.wikitolearn.midtier.course.event.ChapterUpdated;
import org.wikitolearn.midtier.course.event.CourseDeleted;
import org.wikitolearn.midtier.course.event.PageDeleted;
import org.wikitolearn.midtier.course.event.PageUpdated;
import org.wikitolearn.midtier.course.exception.InvalidResourceCreateException;
import org.wikitolearn.midtier.course.exception.InvalidResourceUpdateException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

  @Autowired
  private ObjectMapper objectMapper;

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
        saved = this.save(c);
        c.setId(saved.getId());
        c.setVersion(saved.getVersion());
      } catch (JsonProcessingException e) {
        log.error(e.getMessage());
      }
    });
    return chapters;
  }

  public Chapter findByPageId(String pageId) throws JsonProcessingException {
    EntityList<Chapter> chapters = chapterClient.findByPageId(pageId);
    return chapters.getItems().get(0);
  }

  public Chapter save(Chapter chapter) throws JsonProcessingException {
    if(chapter.getPages() != null && !chapter.getPages().isEmpty()) {
      chapter.setPages(pageService.save(chapter.getPages()));
    }
    return chapterClient.store(chapter);
  }

  public Chapter update(Chapter chapter) throws JsonProcessingException {
    Chapter updatedChapter = chapterClient.update(chapter);
    applicationEventPublisher.publishEvent(new ChapterUpdated(this, updatedChapter));
    return updatedChapter;
  }

  public void delete(Chapter chapter, boolean isBulkDelete) {
    MultiValueMap<String, String> chapterParams = new LinkedMultiValueMap<>();
    ObjectNode projectionJsonObject = objectMapper.getNodeFactory().objectNode().put("title", 1).put("pages", 1);
    try {
      chapterParams.add("projection", objectMapper.writeValueAsString(projectionJsonObject));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }

    chapter = this.find(chapter.getId(), chapterParams);
    chapterClient.delete(chapter);
    applicationEventPublisher.publishEvent(new ChapterDeleted(this, chapter, isBulkDelete));
  }

  public Chapter updatePages(Chapter chapter) throws JsonProcessingException, InvalidResourceCreateException {
    List<Page> currentPages = this.find(chapter.getId(), null).getPages();

    if(currentPages.size() == chapter.getPages().size() && currentPages.containsAll(chapter.getPages())) {
      return this.update(chapter);
    } else {
      log.warn("Invalid chapter's pages update request");
      throw new InvalidResourceUpdateException("Invalid chapter's pages update request");
    }
  }

  public Chapter addPages(Chapter chapter) throws JsonProcessingException, InvalidResourceCreateException {
    List<Page> currentPages = this.find(chapter.getId(), null).getPages();
    List<Page> pagesToAdd = new ArrayList<>(CollectionUtils.disjunction(chapter.getPages(), currentPages));

    if(pagesToAdd.size() != 1) {
      log.warn("Invalid chapter's pages create request");
      throw new InvalidResourceCreateException("Invalid chapter's pages create request");
    }

    Page addedPage = pageService.save(pagesToAdd.get(0));

    chapter.getPages()
      .parallelStream()
      .filter(p -> pagesToAdd.get(0).getTitle().equals(p.getTitle()))
      .findFirst()
      .map(p -> {
        p.setVersion(addedPage.getLatestVersion());
        p.setId(addedPage.getId());
        return p;
      });

    Chapter addedChapter = this.update(chapter);
    updatedChapter.setPages(chapter.getPages());
    return updatedChapter;
  }

  @EventListener
  public void handlePageUpdatedEvent(PageUpdated event) throws JsonProcessingException {
    Page updatedPage = event.getPage();
    Chapter chapter = this.findByPageId(updatedPage.getId());

    chapter.getPages().stream().forEach(p -> {
      if(p.getId().equals(updatedPage.getId())) {
        p.setVersion(updatedPage.getLatestVersion());
      }
    });

    this.update(chapter);
  }

  @EventListener
  public void handlePageDeletedEvent(PageDeleted event) throws JsonProcessingException {
    if(!event.isBulkDelete()) {
      Page deletedPage = event.getPage();
      Chapter chapter = this.findByPageId(deletedPage.getId());

      List<Page> pages = chapter.getPages()
          .stream()
          .sequential()
          .filter(removeDeletedPage(deletedPage.getId()))
          .collect(Collectors.<Page>toList());
      chapter.setPages(pages);

      this.update(chapter);
    }
  }

  @EventListener
  public void handleCourseDeletedEvent(CourseDeleted event) {
    Course deletedCourse = event.getCourse();
    deletedCourse.getChapters().parallelStream().forEach(c -> this.delete(c, true));
  }

  private static Predicate<Page> removeDeletedPage(String deletedPageId) {
    return p -> !deletedPageId.equals(p.getId());
  }

}

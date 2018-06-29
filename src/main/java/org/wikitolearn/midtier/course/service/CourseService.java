package org.wikitolearn.midtier.course.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.wikitolearn.midtier.course.client.CourseClient;
import org.wikitolearn.midtier.course.entity.Chapter;
import org.wikitolearn.midtier.course.entity.Course;
import org.wikitolearn.midtier.course.entity.EntityList;
import org.wikitolearn.midtier.course.event.ChapterDeleted;
import org.wikitolearn.midtier.course.event.ChapterUpdated;
import org.wikitolearn.midtier.course.exception.InvalidResourceCreateException;
import org.wikitolearn.midtier.course.exception.InvalidResourceUpdateException;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseService {

  @Autowired
  private CourseClient courseClient;
  @Autowired
  private ChapterService chapterService;

  public EntityList<Course> findAll(MultiValueMap<String, String> params) {
    return courseClient.findAll(params);
  }

  public Course find(String courseId, MultiValueMap<String, String> params) {
    return courseClient.find(courseId, params);
  }

  public Course findByChapterId(String chapterId) {
    EntityList<Course> courses = courseClient.findByChapterId(chapterId);
    return courses.getItems().get(0);
  }
  
  public EntityList<Course> getAllCourseVersions(String courseId, MultiValueMap<String, String> params) {
    return courseClient.getAllCourseVersions(courseId, params);
  }

  public Course save(Course course) throws JsonProcessingException {
    if(course.getChapters()!= null && !course.getChapters().isEmpty()) {
      course.setChapters(chapterService.save(course.getChapters()));
    }
    return courseClient.save(course);
  }

  public Course update(Course course) throws JsonProcessingException {
    return courseClient.update(course);
  }

  public Course delete(Course course) throws JsonProcessingException {
    course = this.find(course.getId(), null);
    course.getChapters().stream().forEachOrdered(c -> {
      try {
        this.chapterService.delete(c, true);
      } catch (JsonProcessingException e) {
        // FIXME
        log.error(e.getMessage());
      }
    });

    return courseClient.delete(course);
  }

  public Course updateChapters(Course course) throws JsonProcessingException, InvalidResourceCreateException {
    List<Chapter> currentChapters = this.find(course.getId(), null).getChapters();

    if(currentChapters.size() == course.getChapters().size() && currentChapters.containsAll(course.getChapters())) {
      return courseClient.update(course);
    } else {
      log.warn("Invalid course's chapters update request");
      throw new InvalidResourceUpdateException("Invalid course's chapters update request");
    }
  }

  public Course addChapters(Course course) throws JsonProcessingException, InvalidResourceCreateException {
    List<Chapter> currentChapters = this.find(course.getId(), null).getChapters();
    List<Chapter> chaptersToAdd = new ArrayList<>(CollectionUtils.disjunction(course.getChapters(), currentChapters));

    if(chaptersToAdd.size() != 1) {
      log.warn("Invalid course's chapter create request");
      throw new InvalidResourceCreateException("Invalid course's chapter create request");
    }

    Chapter addedChapter = chapterService.save(chaptersToAdd.get(0));

    course.getChapters().stream().forEachOrdered(c -> {
      if(chaptersToAdd.get(0).getTitle().equals(c.getTitle())) {
        c.setVersion(addedChapter.getLatestVersion());
        c.setId(addedChapter.getId());
      }
    });
    return this.update(course);
  }

  @EventListener
  public void handleChapterUpdatedEvent(ChapterUpdated event) throws JsonProcessingException {
    Chapter updatedChapter = event.getChapter();
    Course course = this.findByChapterId(updatedChapter.getId());

    course.getChapters().stream().forEach(c -> {
      if(c.getId().equals(updatedChapter.getId())) {
        c.setVersion(updatedChapter.getLatestVersion());
      }
    });

    this.update(course);
  }

  @EventListener
  public void handleChapterDeletedEvent(ChapterDeleted event) throws JsonProcessingException {
    Chapter deletedChapter = event.getChapter();
    Course course = this.findByChapterId(deletedChapter.getId());

    List<Chapter> chapters = course.getChapters()
        .stream()
        .sequential()
        .filter(removeDeletedChapter(deletedChapter.getId()))
        .collect(Collectors.<Chapter>toList());
    course.setChapters(chapters);

    this.update(course);
  }

  private static Predicate<Chapter> removeDeletedChapter(String deletedChapterId) {
    return c -> !deletedChapterId.equals(c.getId());
  }

}

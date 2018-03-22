package org.wikitolearn.midtier.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.wikitolearn.midtier.course.client.CourseClient;
import org.wikitolearn.midtier.course.entity.Chapter;
import org.wikitolearn.midtier.course.entity.Course;
import org.wikitolearn.midtier.course.entity.EntityList;
import org.wikitolearn.midtier.course.event.ChapterUpdated;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseService {

  @Autowired
  private CourseClient courseClient;
  @Autowired
  private ChapterService chapterService;

  public EntityList<Course> findAll() {
    return courseClient.findAll();
  }

  public Course find(String courseId, MultiValueMap<String, String> params) {
    return courseClient.find(courseId, params);
  }
  
  public Course findByChapterId(String chapterId) {
    EntityList<Course> courses = courseClient.findByChapterId(chapterId);
    return courses.getItems().get(0);
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

}

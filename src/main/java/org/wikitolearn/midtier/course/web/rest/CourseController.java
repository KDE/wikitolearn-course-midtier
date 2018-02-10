package org.wikitolearn.midtier.course.web.rest;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wikitolearn.midtier.course.entity.Course;
import org.wikitolearn.midtier.course.entity.EntityList;
import org.wikitolearn.midtier.course.service.ChapterService;
import org.wikitolearn.midtier.course.service.CourseService;
import org.wikitolearn.midtier.course.service.PageService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/courses")
public class CourseController {

  @Autowired
  private CourseService courseService;

  @Autowired
  private ChapterService chapterService;

  @Autowired
  private PageService pageService;

  @GetMapping(produces = "application/json")
  public EntityList<Course> getCourses() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("projection", "{\"title\":1}");

    EntityList<Course> courses = courseService.findAll();
    courses.get_items().forEach(course -> {
      course.setChapters(
          course.getChapters()
          .parallelStream()
          .map(chapter -> chapterService.find(chapter.get_id(), params))
          .collect(Collectors.toList()));
    });
    return courses;
  }

  @GetMapping(value = "/{courseId}", produces = "application/json")
  public Course getCourse(@PathVariable String courseId) {
    MultiValueMap<String, String> chapterParams = new LinkedMultiValueMap<>();
    chapterParams.add("projection", "{\"title\":1, \"pages\":1}");
    
    MultiValueMap<String, String> pagesParams = new LinkedMultiValueMap<>();
    pagesParams.add("projection", "{\"title\":1}");

    Course course = courseService.find(courseId, null);
    course.setChapters(
        course.getChapters()
        .parallelStream()
        .map(chapter -> {
          chapter = chapterService.find(chapter.get_id(), chapterParams);
          chapter.setPages(
              chapter.getPages()
              .parallelStream()
              .map(page -> pageService.find(page.get_id(), pagesParams))
              .collect(Collectors.toList()));
          return chapter;
        })
        .collect(Collectors.toList()));
    return course;
  }
}

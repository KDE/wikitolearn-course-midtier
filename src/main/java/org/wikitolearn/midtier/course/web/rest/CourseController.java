package org.wikitolearn.midtier.course.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.wikitolearn.midtier.course.service.CourseService;

@RestController
public class CourseController {

  @Autowired
  private CourseService courseService;

  @GetMapping(value="/api/courses", produces="application/json")
  public String getCourses() {
    return courseService.findAll();
  }

  @GetMapping(value="/api/courses/{courseId}", produces="application/json")
  public String getCourse(@PathVariable String courseId) {
    return courseService.find(courseId);
  }
}

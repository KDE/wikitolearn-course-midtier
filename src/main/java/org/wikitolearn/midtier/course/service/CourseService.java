package org.wikitolearn.midtier.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.wikitolearn.midtier.course.client.CourseClient;
import org.wikitolearn.midtier.course.entity.Course;
import org.wikitolearn.midtier.course.entity.EntityList;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseService {

  @Autowired
  private CourseClient courseClient;

  public EntityList<Course> findAll() {
    return courseClient.findAll();
  }

  public Course find(String courseId, MultiValueMap<String, String> params) {
    return courseClient.find(courseId, params);
  }
}

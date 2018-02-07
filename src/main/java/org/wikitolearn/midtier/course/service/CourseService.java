package org.wikitolearn.midtier.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wikitolearn.midtier.course.client.CourseClient;

@Service
public class CourseService {

  @Autowired
  private CourseClient courseClient;

  public String findAll() {
    return courseClient.findAll();
  }

  public String find(String courseId) {
    return courseClient.find(courseId);
  }
}

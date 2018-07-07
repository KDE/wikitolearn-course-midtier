package org.wikitolearn.midtier.course.event;

import org.wikitolearn.midtier.course.entity.Course;

public class CourseDeleted extends CourseEvent {

  private static final long serialVersionUID = -3306494922543843520L;

  public CourseDeleted(Object source, Course course) {
    super(source, course);
  }
}

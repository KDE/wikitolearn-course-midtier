package org.wikitolearn.midtier.course.event;

import org.springframework.context.ApplicationEvent;
import org.wikitolearn.midtier.course.entity.Course;

public class CourseEvent extends ApplicationEvent {
  
  private static final long serialVersionUID = 6567901075806476046L;
  private Course course;
  
  public CourseEvent(Object source, Course course) {
    super(source);
    this.course = course;
  }
  
  public Course getCourse() {
    return this.course;
  }

}
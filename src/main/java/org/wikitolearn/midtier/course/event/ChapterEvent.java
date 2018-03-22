package org.wikitolearn.midtier.course.event;

import org.springframework.context.ApplicationEvent;
import org.wikitolearn.midtier.course.entity.Chapter;

public class ChapterEvent extends ApplicationEvent {

  private static final long serialVersionUID = -8615411306119703686L;
  
  private Chapter chapter;
  public ChapterEvent(Object source, Chapter chapter) {
    super(source);
    this.chapter = chapter;
  }
  
  public Chapter getChapter() {
    return this.chapter;
  }

}

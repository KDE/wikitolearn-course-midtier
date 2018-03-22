package org.wikitolearn.midtier.course.event;

import org.wikitolearn.midtier.course.entity.Chapter;

public class ChapterUpdated extends ChapterEvent {

  private static final long serialVersionUID = -5227779939148887965L;

  public ChapterUpdated(Object source, Chapter chapter) {
    super(source, chapter);
  }

}

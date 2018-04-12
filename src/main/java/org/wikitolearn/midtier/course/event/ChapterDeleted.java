package org.wikitolearn.midtier.course.event;

import org.wikitolearn.midtier.course.entity.Chapter;

public class ChapterDeleted extends ChapterEvent {

  private static final long serialVersionUID = -4659266761051494979L;

  public ChapterDeleted(Object source, Chapter chapter) {
    super(source, chapter);
  }

}

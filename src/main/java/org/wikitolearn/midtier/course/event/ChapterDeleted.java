package org.wikitolearn.midtier.course.event;

import org.wikitolearn.midtier.course.entity.Chapter;

public class ChapterDeleted extends ChapterEvent {

  private static final long serialVersionUID = -4659266761051494979L;
  private boolean bulkDelete;

  public ChapterDeleted(Object source, Chapter chapter, boolean bulkDelete) {
    super(source, chapter);
    this.bulkDelete = bulkDelete;
  }
  
  public boolean isBulkDelete() {
    return this.bulkDelete;
  }
}

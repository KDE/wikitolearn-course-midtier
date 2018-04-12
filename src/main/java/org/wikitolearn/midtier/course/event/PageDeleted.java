package org.wikitolearn.midtier.course.event;

import org.wikitolearn.midtier.course.entity.Page;

public class PageDeleted extends PageEvent {

  private static final long serialVersionUID = -4659266761051494979L;

  public PageDeleted(Object source, Page page) {
    super(source, page);
  }

}

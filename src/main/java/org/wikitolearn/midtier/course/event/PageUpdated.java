package org.wikitolearn.midtier.course.event;

import org.wikitolearn.midtier.course.entity.Page;

public class PageUpdated extends PageEvent {

  private static final long serialVersionUID = 785072822009096025L;
  
  public PageUpdated(Object source, Page page) {
    super(source, page);
  }
}

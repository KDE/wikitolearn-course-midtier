package org.wikitolearn.midtier.course.event;

import org.wikitolearn.midtier.course.entity.Page;

public class PageDeleted extends PageEvent {

  private static final long serialVersionUID = -4659266761051494979L;
  private boolean bulkDelete;

  public PageDeleted(Object source, Page page, boolean bulkDelete) {
    super(source, page);
    this.bulkDelete = bulkDelete;
  }
  
  public boolean isBulkDelete() {
    return this.bulkDelete;
  }

}

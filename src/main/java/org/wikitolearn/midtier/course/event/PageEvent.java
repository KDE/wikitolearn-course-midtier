package org.wikitolearn.midtier.course.event;

import org.springframework.context.ApplicationEvent;
import org.wikitolearn.midtier.course.entity.Page;

public class PageEvent extends ApplicationEvent {

  private static final long serialVersionUID = -3146551492041153535L;
  
  private Page page;
  public PageEvent(Object source, Page page) {
    super(source);
    this.page = page;
  }
  
  public Page getPage() {
    return this.page;
  }

}
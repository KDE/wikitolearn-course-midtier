package org.wikitolearn.midtier.course.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@RequiredArgsConstructor
@Getter
@Setter
public class Page extends Entity {
  
  private String content;
}

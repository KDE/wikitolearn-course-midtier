package org.wikitolearn.midtier.course.entity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
public class Course extends Entity {
  private List<Chapter> chapters;
  
  public String toSchemaCompliant() throws JsonProcessingException {
    Optional
    .ofNullable(this.chapters)
    .orElseGet(Collections::emptyList)
    .stream().forEachOrdered(c -> {c.setTitle(null); c.setLanguage(null); c.setPages(null);});
    this.setEtag(null);
    this.setCreated(null);
    this.setUpdated(null);
    this.setVersion(0);
    this.setLatestVersion(0);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}

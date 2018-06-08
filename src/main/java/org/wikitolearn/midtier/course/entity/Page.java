package org.wikitolearn.midtier.course.entity;

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
public class Page extends Entity {
  
  private String content;
  
  public String toSchemaCompliant() throws JsonProcessingException {
    this.setEtag(null);
    this.setCreated(null);
    this.setUpdated(null);
    this.setVersion(0);
    this.setLatestVersion(0);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}

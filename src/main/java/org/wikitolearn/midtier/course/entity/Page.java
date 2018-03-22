package org.wikitolearn.midtier.course.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Page extends Entity {
  
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

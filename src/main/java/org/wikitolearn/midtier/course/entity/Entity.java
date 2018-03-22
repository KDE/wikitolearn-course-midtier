package org.wikitolearn.midtier.course.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_DEFAULT)
public abstract class Entity {
    
    @JsonProperty("_id")
    private String id;
    
    @JsonProperty("_etag")
    private String etag;
    
    @JsonProperty("_version")
    private int version;
    
    @JsonProperty("_latest_version")
    private int latestVersion;
    
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss z")
    @JsonProperty("_updated")
    private Date updated;
    
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss z")
    @JsonProperty("_created")
    private Date created;
    
    @JsonProperty("_deleted")
    private boolean deleted;
    
    private String title;
    
    private String content;
    
    private List<String> authors;
    
    private String language;
    
    public String toJson() throws JsonProcessingException {
      ObjectMapper mapper = new ObjectMapper(); 
      return mapper.writeValueAsString(this);
    }
}

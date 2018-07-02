package org.wikitolearn.midtier.course.entity.dto.in;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.wikitolearn.midtier.course.entity.Link;
import org.wikitolearn.midtier.course.entity.Meta;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_DEFAULT)
public class GetCourseVersionsDto {
  @JsonProperty("_items")
  List<CourseVersionInListDto> items;

  @JsonProperty("_meta")
  Meta meta;

  @JsonProperty("_links")
  Map<String, Link> links;

  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(Include.NON_DEFAULT)
  public static final class CourseVersionInListDto {
    @JsonProperty("_id")
    private String id;

    @JsonProperty("_etag")
    private String etag;

    @JsonProperty("_version")
    private int version;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss z")
    @JsonProperty("_updated")
    private Date updated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss z")
    @JsonProperty("_created")
    private Date created;

    private String title;

    private List<String> authors;

    private String language;

  }
}

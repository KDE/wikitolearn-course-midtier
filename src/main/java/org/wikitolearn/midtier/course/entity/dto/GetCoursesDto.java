package org.wikitolearn.midtier.course.entity.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.wikitolearn.midtier.course.entity.Link;
import org.wikitolearn.midtier.course.entity.Meta;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_DEFAULT)
public class GetCoursesDto {
  @JsonProperty("_items")
  List<CourseInListDto> items;

  @JsonProperty("_meta")
  Meta meta;

  @JsonProperty("_links")
  Map<String, Link> links;

  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(Include.NON_DEFAULT)
  public static final class CourseInListDto {
    @JsonProperty("_id")
    private String id;

    @JsonProperty("_etag")
    private String etag;

    @JsonProperty("_version")
    private int version;

    @JsonProperty("_latest_version")
    private int latestVersion;

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
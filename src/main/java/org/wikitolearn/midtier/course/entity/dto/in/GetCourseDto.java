package org.wikitolearn.midtier.course.entity.dto.in;

import java.util.Date;
import java.util.List;

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
public class GetCourseDto {
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

  private List<ChapterInCourse> chapters;

  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(Include.NON_DEFAULT)
  public static final class ChapterInCourse {
    @JsonProperty("_id")
    private String id;

    @JsonProperty("_version")
    private int version;

    private String title;

    private List<PageInChapter> pages;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(Include.NON_DEFAULT)
    public static final class PageInChapter {
      @JsonProperty("_id")
      private String id;

      @JsonProperty("_version")
      private int version;

      private String title;
    }
  }
}

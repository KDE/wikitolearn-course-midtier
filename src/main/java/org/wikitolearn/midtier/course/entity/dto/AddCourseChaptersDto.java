package org.wikitolearn.midtier.course.entity.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_DEFAULT)
public class AddCourseChaptersDto {

  private String title;
  private String language;
  private List<ChapterInAddCourseChapters> chapters;

  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(Include.NON_DEFAULT)
  public static final class ChapterInAddCourseChapters {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_version")
    private int version;

    @ApiModelProperty(value = "This property is needed only for the new chapter", required = false)
    private String title;
    @ApiModelProperty(value = "This property is needed only for the new chapter", required = false)
    private String language;
  }
}

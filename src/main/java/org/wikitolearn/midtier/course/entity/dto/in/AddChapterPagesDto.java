package org.wikitolearn.midtier.course.entity.dto.in;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_DEFAULT)
public class AddChapterPagesDto {
  @NotNull
  private String title;
  @NotNull
  private String language;
  @NotEmpty
  @NotNull
  private List<PageInAddChapterPages> pages;

  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(Include.NON_DEFAULT)
  public static final class PageInAddChapterPages {
    @JsonProperty("_id")
    @NotNull
    private String id;
    @JsonProperty("_version")
    @NotNull
    private int version;

    @ApiModelProperty(value = "This property is needed only for the new page", required = false)
    private String title;
    @ApiModelProperty(value = "This property is needed only for the new page", required = false)
    private String language;
    @ApiModelProperty(value = "This property is needed only for the new page", required = false)
    private String content;
  }
}

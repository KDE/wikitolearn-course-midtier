package org.wikitolearn.midtier.course.entity.dto.in;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class UpdateChapterPagesDto {
  @NotNull
  private String title;
  @NotNull
  private String language;
  @NotNull
  @NotEmpty
  private List<PageInUpdateChapterPages> pages;
  
  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(Include.NON_DEFAULT)
  public static final class PageInUpdateChapterPages {
    @JsonProperty("_id")
    @NotNull
    private String id;

    @JsonProperty("_version")
    @NotNull
    private int version;
  }
}

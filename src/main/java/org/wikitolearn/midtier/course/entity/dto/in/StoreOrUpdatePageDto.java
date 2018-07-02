package org.wikitolearn.midtier.course.entity.dto.in;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_DEFAULT)
public class StoreOrUpdatePageDto {
  @NotNull
  private String title;
  @NotNull
  private String language;
  @NotNull
  private String content;
}

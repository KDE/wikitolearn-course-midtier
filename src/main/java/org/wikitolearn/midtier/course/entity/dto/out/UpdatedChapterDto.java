package org.wikitolearn.midtier.course.entity.dto.out;

import java.util.Date;

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
public class UpdatedChapterDto {
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
}

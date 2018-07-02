package org.wikitolearn.midtier.course.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public abstract class Entity {
    
    @JsonProperty("_id")
    private String id;
    
    @ApiModelProperty( readOnly = true )
    @JsonProperty("_etag")
    private String etag;
    
    @JsonProperty("_version")
    private int version;
    
    @ApiModelProperty( readOnly = true )
    @JsonProperty("_latest_version")
    private int latestVersion;
    
    @ApiModelProperty( readOnly = true )
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss z")
    @JsonProperty("_updated")
    private Date updated;
    
    @ApiModelProperty( readOnly = true )
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss z")
    @JsonProperty("_created")
    private Date created;
    
    @ApiModelProperty( readOnly = true )
    @JsonProperty("_deleted")
    private boolean deleted;
    
    private String title;
    
    @ApiModelProperty( readOnly = true )
    private List<String> authors;
    
    private String language;
}

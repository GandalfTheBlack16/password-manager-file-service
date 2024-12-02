package com.gandalftheblack.pm.fileservice.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gandalftheblack.pm.fileservice.model.entity.FileStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileGetResponse {
    @JsonProperty("file_id")
    private String id;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("mime_type")
    private String mimeType;
    @JsonProperty("file_size")
    private Long fileSize;
    private String path;
    private FileStatus status;
    @JsonProperty("creation_date")
    private Date creationDate;
    @JsonProperty("modification_date")
    private Date modificationDate;
    @JsonProperty("deletion_date")
    private Date deletionDate;
}

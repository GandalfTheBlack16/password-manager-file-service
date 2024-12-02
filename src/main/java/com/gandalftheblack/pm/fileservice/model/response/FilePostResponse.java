package com.gandalftheblack.pm.fileservice.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilePostResponse {
    @JsonProperty("file_id")
    private String fileId;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("mime_type")
    private String mimeType;
    private String status;
    @JsonProperty("creation_date")
    private String creationDate;
}

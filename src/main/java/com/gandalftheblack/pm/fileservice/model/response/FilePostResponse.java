package com.gandalftheblack.pm.fileservice.model.response;

import lombok.Data;

@Data
public class FilePostResponse {
    private String fileId;
    private String fileName;
    private String mimeType;
    private String status;
    private String creationDate;
}

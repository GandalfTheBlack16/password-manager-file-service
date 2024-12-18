package com.gandalftheblack.pm.fileservice.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FilePostResponse extends HttpBaseResponse{
    private String fileId;
    private String fileName;
    private String mimeType;
    private String status;
    private String creationDate;
}
